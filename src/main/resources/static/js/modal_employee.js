function employeeDetails(event, restaurants) {
    const id = parseInt(event.target.getAttribute("attr-id"));
    const modal = document.getElementById("myModal-details")
    modal.style.display = "block";
    console.log(id)
    const result = restaurants.find(item => {
        return item.id === id
    });

    document.querySelector("#myModal-details #modal-details-content").innerHTML = `
       <h2>Détails de l'employé</h2>
       <img class="image_details" src="data:image/png;base64,${result.image}">
       <p><strong>Nom:</strong> ${result.name}  ${result.surname}</p>
       <p><strong>Téléphone:</strong> ${result.phone}  </p>
       <p><strong>Mail:</strong> ${result.mail} </p>
       <p><strong>Date Embauche:</strong> ${result.hireDate}  </p>

       <h3>Restaurant :</h3>
       <ul>
           ${result.assignements.map(a => `<li>${a.restaurant.name} - ${a.responsability.role}</li>`).join('')}
       </ul>
   `;
}

function employeeEdit(event, restaurants) {
    console.log("Edit restaurant called");
    const id = parseInt(event.target.getAttribute("attr-id"));
    const modal = document.getElementById("myModal-edit")
    modal.style.display = "block";
    const result = restaurants.find(item => {
        return item.id === id
    });
    console.log(result)
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
        document.getElementById("myModal-edit").style.display = "block";
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


    document.getElementById("formAddEntity").addEventListener("submit", function(e) {
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