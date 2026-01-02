function assignementDetails(event, restaurants) {
    const id = parseInt(event.target.getAttribute("attr-id"));
    const modal = document.getElementById("myModal-details")
    modal.style.display = "block";
    console.log(id)
    const result = restaurants.find(item => {
        return item.id === id
    });

    document.querySelector("#myModal-details #modal-details-content").innerHTML = `
       <h2>Détails de l'assignement</h2>
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



function assignementRemove() {
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


    document.getElementById("formAddEntity").addEventListener("submit", function (e) {
        e.preventDefault(); // empêche le submit classique

        const formData = new FormData(document.getElementById("formAddEntity"));

        fetch("/assignement/save", {
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