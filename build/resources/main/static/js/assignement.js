let limit = 10; // 10 results par page
let pageNumber = 1;

let assignement = [];

document.addEventListener("DOMContentLoaded", function () {

    / * function Handle assignement  */

    function searchEvents() {
        document.getElementById("searchInput").addEventListener("input", (event) => {
            pageNumber = 1; // Reset to first page on new search
            searchAssignements()
        });
        document.getElementById("searchInputDate").addEventListener("input", (event) => {
            document.getElementById("searchInput").value=document.getElementById("searchInputDate").value
            searchAssignements()
        })

        document.getElementById("filterWrapper").addEventListener("change", (event) => {
             if (event.target.value.includes(":Date")) {
                    document.getElementById("searchInput").style.display = "none";
                    document.getElementById("searchInputDate").style.display =  "block";

                } else {
                    document.getElementById("searchInput").style.display =  "block";
                    document.getElementById("searchInputDate").style.display = "none";
                }
            document.getElementById("searchInput").value=""
            document.getElementById("searchInputDate").value=""
            pageNumber = 1; // Reset to first page on new search
            searchAssignements()
        });
    }

    searchEvents()

    function searchAssignements() {
        const filter = document.getElementById("filterWrapper").value;
        const query = document.getElementById("searchInput").value;
        countAssignements(filter, query)
        console.log("Searching assignements with query:", query);
        const xhr = new XMLHttpRequest();
        const params = new URLSearchParams({ filter, filter, query: query, limit: limit, offset: pageNumber - 1 });
        xhr.open("GET", "/assignement/search?" + params.toString(), true);

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) { // 4 = DONE
                if (xhr.status === 200) {
                    assignements = JSON.parse(xhr.responseText);

                    // Array //
                    let container = document.getElementById('tableRow');
                    container.innerHTML = "";
                    container.innerHTML = assignements.map(a => `
                    <tr attr-id="${a.employee.id}_${a.restaurant.id}">
                    <td>${a.employee.name} ${a.employee.surname}</td>
                    <td>${a.responsability.role}</td>
                    <td>${a.restaurant.name}</td>
                    <td>${displayDate(a.startDate)}</td>
                    <td>${a.endDate == undefined ? "X" : displayDate(a.endDate)}</td>
                    <td>
                        <button  attr-id="${a.employee.id}_${a.restaurant.id}" class="assignementDetails fas fa-eye" onclick="assignementDetails(event,assignements)"></button>
                        <button attr-id="${a.employee.id}_${a.restaurant.id}" class="assignementEdit edit-btn fas fa-pen" onclick="assignementEdit(event,assignements)"></button>
                        <button  attr-id="${a.employee.id}_${a.restaurant.id}" class="assignementRemove remove-btn fas fa-trash" onclick="assignementRemove(event,assignements)"></button>
                    </td>
                    </tr>
                    `).join('');
                } else {
                    console.error("Request failed with status:", xhr.status);
                }
            }
        }
        xhr.send();
    }

    /* Count assignements for pagination */
    function countAssignements(filter, query) {
        const xhr = new XMLHttpRequest();
        const params = new URLSearchParams({ filter, filter, query: query });
        xhr.open("GET", "/assignement/count?" + params.toString(), true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) { // 4 = DONE
                if (xhr.status === 200) {
                    console.log(xhr.responseText)
                    const count = JSON.parse(xhr.responseText);

                    // Pagination //
                    let pagination = document.getElementById('pagination');
                    pagination.innerHTML = "";
                    for (let i = 1; i <= Math.ceil(count / limit); i++) {
                        const pageLink = document.createElement('button');
                        pageLink.innerText = i;
                        if (i == pageNumber) pageLink.classList.add("page-active");
                        pageLink.addEventListener('click', () => {
                            pageNumber = i;
                            searchAssignements(document.getElementById("searchInput").value, limit, this.textContent - 1);
                        });
                        pagination.appendChild(pageLink);
                    }
                } else {
                    console.error("Request failed with status:", xhr.status);
                }
            }
        }
        xhr.send();
    }


    function init() {
        searchAssignements("", limit, pageNumber - 1)
    }
    init();

});