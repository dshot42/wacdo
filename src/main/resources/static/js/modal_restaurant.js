function restaurantDetails(event, restaurants) {
    const id = parseInt(event.target.getAttribute("attr-id"));
    const modal = document.getElementById("myModal-details")
    modal.style.display = "block";
    console.log(id)
    const result = restaurants.find(item => {
        return item.id === id
    });

    document.querySelector("#myModal-details #modal-details-content").innerHTML = `
       <h2>Détails du restaurant</h2>
       <img class="image_details" src="data:image/png;base64,${result.image}">
       <p><strong>Nom:</strong> ${result.name}</p>
       <p><strong>Adresse:</strong> ${result.restaurantAddress.address}, ${result.restaurantAddress.postalCode} ${result.restaurantAddress.city}</p>
       <h3>Employees:</h3>
       <ul>
           ${result.assignements.map(a => `<li>${a.employee.name} - ${a.responsability.role}</li>`).join('')}
       </ul>
   `;
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

document.querySelector('input[name="restaurantAddress.address"]').addEventListener('input', (event) => {
    const modal = document.getElementById("myModal-edit")
    const address = event.target.value + ', ' +
        modal.querySelector('input[name="restaurantAddress.postalCode"]').value + ' ' +
        modal.querySelector('input[name="restaurantAddress.city"]').value;
    fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(address)}`)
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                 modal.querySelector('input[name="restaurantAddress.cordX"]').value = data[0].lat;
                 modal.querySelector('input[name="restaurantAddress.cordY"]').value =  data[0].lon;
                console.log(`Latitude: ${lat}, Longitude: ${lon}`);
            } else {
                console.log("Address not found");
                alert("Adresse non trouvée. Veuillez vérifier l'adresse saisie.");
            }
        })
        .catch(error => console.error(error));
    });



    document.getElementById("formAddEntity").addEventListener("submit", function(e) {
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