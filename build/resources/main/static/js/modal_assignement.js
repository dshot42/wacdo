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

function assignementEdit(event, assignements) {
    console.log("Edit assignement called");
    const id = event.target.getAttribute("attr-id");
    console.log(id)
    const modal = document.getElementById("myModal-edit")
    modal.style.display = "block";
    const result = assignements.find(item => {
        return item.employee.id+"_"+item.restaurant.id === id
    });
    console.log(result)
    modal.querySelector('#searchEmployeeInput').value  =   result.restaurant.name;
    modal.querySelector('#searchRestaurantInput').value  =   result.employee.name +" "+ result.employee.surname

    modal.querySelector('input[name="id.restaurantId"]').value = result.restaurant.id;
    modal.querySelector('input[name="id.employeeId"]').value = result.employee.id;
    modal.querySelector('select[name="responsability.id"]').value = result.responsability.id;
    modal.querySelector('input[name="startDate"]').value = result.startDate;
   modal.querySelector('input[name="endDate"]').value = result.endDate == undefined ? '' :  result.endDate ;
}


function assignementRemove (event, assignements) {
   const id = event.target.getAttribute("attr-id");
   const result = assignements.find(item => {
       return item.employee.id+"_"+item.restaurant.id === id
   });

    const formData = new FormData();
    formData.append("employeeId", result.employee.id);
    formData.append("restaurantId", result.restaurant.id);


    fetch("/assignement/delete", {
       method: "POST",
       body: formData
    })
   .then(res => res.text())
   .then(html => {
       document.getElementById("searchInput").dispatchEvent(new Event("input", { bubbles: true }));
   });
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
            modal.querySelectorAll('input').forEach(input => {
                        input.value = ''
                      })
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