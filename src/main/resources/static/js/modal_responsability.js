function responsabilityDetails(event, restaurants) {
    const id = parseInt(event.target.getAttribute("attr-id"));
    const modal = document.getElementById("myModal-details")
    modal.style.display = "block";
    console.log(id)
    const result = restaurants.find(item => {
        return item.id === id
    });

    document.querySelector("#modal-search-assignement h4").innerHTML = `Rechercher un salarié`;

    document.querySelector("#myModal-details #modal-details-content").innerHTML = `
       <h3>responsabilité</h3>
       <h3>Restaurant :</h3>
       <ul>
           ${result.assignements.map(a => `<li>${a.restaurant.name} - ${a.responsability.role}</li>`).join('')}
       </ul>
   `;

}

function responsabilityEdit(event, restaurants) {
    console.log("Edit restaurant called");
    const id = parseInt(event.target.getAttribute("attr-id"));
    const modal = document.getElementById("myModal-edit")
    modal.style.display = "block";
    const result = restaurants.find(item => {
        return item.id === id
    });

    modal.querySelector('input[name="id"]').value = result.id;
    modal.querySelector('input[name="role"]').value = result.role;

}


document.addEventListener("DOMContentLoaded", function () {

    /* Modal handling */
    let closeBtns = document.getElementsByClassName("modal-close");
    let modals = document.getElementsByClassName("modal");

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

        fetch("/responsability/save", {
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