let selectEmployeeId = 0;

function searchRestaurant() {
    const filter = document.getElementById("filterWrapperModalDetails").value;
    const query = document.getElementById("searchInputModalDetails").value;
    const order = "asc"; // todo
    const xhr = new XMLHttpRequest();
    const params = new URLSearchParams({ filter: filter, query: query,order: order });
    xhr.open("GET", "http://localhost:8080/restaurant/search/employee-id/" + selectEmployeeId + "?" + params.toString(), true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) { // 4 = DONE
            if (xhr.status === 200) {
                result = JSON.parse(xhr.responseText);
                document.getElementById("modal-details-assignement-content").innerHTML = `
                     <table id="tableEntityDetails">
                      <thead>
                            <tr>
                                <th>Nom</th>
                                <th>Responsabilité</th>
                                <th>Date de début contrat</th>
                                <th>Fin de contrat</th>
                            </tr>
                        </thead>
                        <tbody id="tableRowDetails">
                                ${result.assignements.map(a => `<tr>
                                    <td>${a.restaurant.name}  </td>
                                    <td>${a.responsability.role}</td>
                                    <td> ${displayDate(a.startDate)}</td>
                                    <td class="bi bi-check-circle-fill text-success"><span style="margin-left: 10px; white-space: nowrap;">En cours</span> </td>
                                </tr>`).join('')}

                               ${result.oldAssignements.map(a => `<tr>
                                  <td>${a.restaurant.name}  </td>
                                  <td>${a.responsability.role}</td>
                                  <td> ${displayDate(a.startDate)}</td>
                                    <td class="bi bi-x-circle-fill text-danger"><span style="margin-left: 10px; white-space: nowrap;">${displayDate(a.endDate)}</span></td>
                              </tr>`).join('')}
                       </tbody>
                   </table>
                 `;
            } else {
                console.error("Request failed with status:", xhr.status);
            }
        }
    }
    xhr.send();
}

function searchRestaurantEvent() {
    document.getElementById("searchInputModalDetails").addEventListener("input", (event) => {
        searchRestaurant()
    })
    document.getElementById("searchInputDateModalDetails").addEventListener("input", (event) => {
    document.getElementById("searchInputModalDetails").value=document.getElementById("searchInputDateModalDetails").value
        searchRestaurant()
    })
    document.getElementById("filterWrapperModalDetails").addEventListener("change", (event) => {

    if (event.target.value.includes(":Date")) {
        console.log(event.target.value)
        document.getElementById("searchInputModalDetails").style.display = "none";
        document.getElementById("searchInputDateModalDetails").style.display =  "block";
        document.getElementById("searchInputModalDetails").value=""
        document.getElementById("searchInputDateModalDetails").value=""
    } else {
        document.getElementById("searchInputModalDetails").style.display =  "block";
        document.getElementById("searchInputDateModalDetails").style.display = "none";
        document.getElementById("searchInputDateModalDetails").value=""
    }
        searchRestaurant();
    });
}
searchRestaurantEvent()

function employeeDetails(event, restaurants) {
    const id = parseInt(event.target.getAttribute("attr-id"));
    selectEmployeeId = id
    const modal = document.getElementById("myModal-details")
    modal.style.display = "block";
    const result = restaurants.find(item => {
        return item.id === id
    });

    document.querySelector("#modal-details-header h2").textContent = "Détails du restaurant";
    document.querySelector("#modal-details-search-title").innerHTML = `Rechercher un restaurant`;
    document.querySelector("#myModal-details #modal-details-content").innerHTML = `
       <h3>Détails de l'employé</h3>
      ${result.image
            ? `<img class="image_details" src="data:image/png;base64,${result.image}"  alt="image">`
            : `<img class="image_details" src="/images/profile.png"  alt="Wacdo logo">`
        }
       <p><strong>Nom:</strong> ${result.name}  ${result.surname}</p>
       <p><strong>Téléphone:</strong> ${result.phone}  </p>
       <p><strong>Mail:</strong> ${result.mail} </p>
       <p><strong>Date Embauche:</strong> ${displayDate(result.hireDate)}  </p>
   `;

    searchRestaurant()
}

function employeeEdit(event, restaurants) {
    console.log("Edit restaurant called");
    const id = parseInt(event.target.getAttribute("attr-id"));
    const modal = document.getElementById("myModal-edit")
    modal.style.display = "block";
    const result = restaurants.find(item => {
        return item.id === id
    });

    modal.querySelector('input[name="id"]').value = result.id;
    modal.querySelector('input[name="name"]').value = result.name;
    modal.querySelector('input[name="surname"]').value = result.surname;
    modal.querySelector('input[name="mail"]').value = result.mail;
    modal.querySelector('input[name="phone"]').value = result.phone;
    modal.querySelector('input[name="hireDate"]').value = result.hireDate;

    document.querySelector("#myModal-edit #image_details").src = `data:image/png;base64,${result.image}`;
    document.querySelector("#myModal-edit #imageBase64").value = result.image;
}


function employeeRemove() {
    // todo
}

document.addEventListener("DOMContentLoaded", function () {

    /* Modal handling */
    let closeBtns = document.getElementsByClassName("modal-close");
    let modals = document.getElementsByClassName("modal");
    const fileInput = document.getElementById('fileInput');

    Array.from(closeBtns).forEach(btn => {
        btn.addEventListener("click", () => {
            Array.from(modals).forEach(modal => {
                modal.style.display = "none";
            });
        });
    });

    /* Modal handling */
    document.getElementById("addButton").addEventListener("click", () => {
        const modal = document.getElementById("myModal-edit");
        modal.style.display = "block";
        modal.querySelector('input[name="id"]').value =null;
    });


    // Convertir image en base64 pour hidden input
    imageInput.addEventListener('change', (event) => {
        console.log("Image input changed");
        const file = event.target.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onload = () => {
            const hiddenInput = document.querySelector('#myModal-edit #imageBase64');
            if (!hiddenInput) {
                console.error("Hidden input not found!");
                return;
            }
            hiddenInput.value = reader.result.split(',')[1]; // Base64 only
            document.querySelector("#myModal-edit #image_details").src = `data:image/png;base64,${hiddenInput.value}`;
        };
        reader.readAsDataURL(file);
    });


    document.getElementById("formAddEntity").addEventListener("submit", function (e) {
        e.preventDefault(); // empêche le submit classique

        const formData = new FormData(document.getElementById("formAddEntity"));

        fetch("/employee/save", {
            method: "POST",
            body: formData
        })
            .then(res => res.text())
            .then(html => {
                document.getElementsByClassName("modal-close")[0].click()
                document.getElementById("searchInput").dispatchEvent(new Event("input", { bubbles: true }));
            });
    });

});