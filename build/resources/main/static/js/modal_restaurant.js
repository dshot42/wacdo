
let selectRestaurantId = 0;

function searchEmployee() {
    const filter = document.getElementById("filterWrapperModalDetails").value;
    const query = document.getElementById("searchInputModalDetails").value;
    const order = "asc";
    const xhr = new XMLHttpRequest();
    const params = new URLSearchParams({ filter: filter, query: query,order: order });
    xhr.open("GET", "/employee/search/restaurant-id/" +selectRestaurantId + "?" + params.toString(), true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) { // 4 = DONE
            if (xhr.status === 200) {
                result = JSON.parse(xhr.responseText);
                document.getElementById("modal-details-assignement-content").innerHTML = `
                     <table id="tableEntityDetails">
                      <thead>
                            <tr>
                                <th>Nom</th>
                                <th>Responsabilite</th>
                                <th>Date de début contrat</th>
                                <th>Fin de contrat</th>
                            </tr>
                        </thead>
                        <tbody id="tableRowDetails">
                                ${result.assignements.map(a => `<tr>
                                    <td>${a.employee.name} ${a.employee.surname} </td>
                                    <td>${a.responsability.role}</td>
                                    <td> ${displayDate(a.startDate)}</td>
                                    <td class="bi bi-check-circle-fill text-success"><span style="margin-left: 10px; white-space: nowrap;">En cours</span> </td>
                                </tr>`).join('')}

                               ${result.oldAssignements.map(a => `<tr>
                                  <td>${a.employee.name} ${a.employee.surname} </td>
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

function searchEmployeeEvent() {
    document.getElementById("searchInputModalDetails").addEventListener("input", (event) => {
        searchEmployee()
    })
    document.getElementById("searchInputDateModalDetails").addEventListener("input", (event) => {
    document.getElementById("searchInputModalDetails").value=document.getElementById("searchInputDateModalDetails").value
        searchEmployee()
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
        searchEmployee();
    });
}
searchEmployeeEvent()

function restaurantDetails(event, restaurants) {
    const id = parseInt(event.target.getAttribute("attr-id"));
    selectRestaurantId = id;

    const modal = document.getElementById("myModal-details")
    modal.style.display = "block";
    console.log(id)
    const result = restaurants.find(item => {
        return item.id === id
    });

    document.querySelector("#modal-details-header h2").textContent = "Détails du restaurant";
    document.querySelector("#myModal-details #modal-details-content").innerHTML = `
        <img class="image_details" style="float: right;" src="${result.image ?
            `data:image/png;base64,${result.image}`
            : "/images/store2.png"}" alt="image resutant">
       <p><strong>Nom:</strong> ${result.name}</p>
       <p><strong>Adresse:</strong> ${result.restaurantAddress.address}, ${result.restaurantAddress.postalCode} ${result.restaurantAddress.city}</p>
   `;

    document.getElementById("modal-details-search-title").innerHTML = `Rechercher un salarié`
    searchEmployee()
}

function restaurantEdit(event, restaurants) {
    console.log("Edit restaurant called");
    const id = parseInt(event.target.getAttribute("attr-id"));
    const modal = document.getElementById("myModal-edit")
    modal.style.display = "block";
    const result = restaurants.find(item => {
        return item.id === id
    });
    console.log(result)
    modal.querySelector('input[name="name"]').value = result.name;
    modal.querySelector('input[name="restaurantAddress.id"]').value = result.restaurantAddress.id;
    modal.querySelector('input[name="restaurantAddress.address"]').value = result.restaurantAddress.address;
    modal.querySelector('input[name="restaurantAddress.city"]').value = result.restaurantAddress.city;
    modal.querySelector('input[name="restaurantAddress.postalCode"]').value = result.restaurantAddress.postalCode;
    modal.querySelector('input[name="id"]').value = result.id;
    document.querySelector("#myModal-edit #image_details").src = `data:image/png;base64,${result.image}`;
    document.querySelector("#myModal-edit #imageBase64").value = result.image;
}


function restaurantRemove() {
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
            console.log(" modal.querySelector('input[name="id"]').value "+ modal.querySelector('input[name="id"]').value)
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

    document.getElementById('searchWrapperEdit').addEventListener('click', (event) => {
        document.getElementById('spinnerSearch').style.display = 'block';
        const modal = document.getElementById("myModal-edit")
        const address = modal.querySelector('input[name="restaurantAddress.address"]').value + ' , ' +
            parseInt(modal.querySelector('input[name="restaurantAddress.postalCode"]').value) + ' ' +
            modal.querySelector('input[name="restaurantAddress.city"]').value;
        fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(address)}`)
            .then(response => response.json())
            .then(data => {
                if (data.length > 0) {
                    modal.querySelector('input[name="restaurantAddress.cordX"]').value = data[0].lat;
                    modal.querySelector('input[name="restaurantAddress.cordY"]').value = data[0].lon;
                     alert("[SUCCESS] Adresse trouvé ! Latitude: "+data[0].lat+", Longitude: "+data[0].lon);
                } else {
                    console.log("Address not found");
                    alert("[ERROR] Adresse non trouvée. Veuillez vérifier l'adresse saisie.");
                }
                document.getElementById('spinnerSearch').style.display = 'none';
            })
            .catch(error => console.error(error));
    });



    document.getElementById("formAddEntity").addEventListener("submit", function (e) {
        e.preventDefault(); // empêche le submit classique

        const formData = new FormData(document.getElementById("formAddEntity"));

        fetch("/restaurant/save", {
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