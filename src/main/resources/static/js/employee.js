let limit = 10; // 10 results par page
let pageNumber = 1;

let employee = [];

document.addEventListener("DOMContentLoaded", function () {
    /*********************************/

    document.getElementById("addButton").addEventListener("input", (event) => {
        const modal = document.getElementById("myModal-edit").style.display = "block";
    });

    / * function Handle employee  */


    document.getElementById("searchInput").addEventListener("input", (event) => {
        pageNumber = 1; // Reset to first page on new search
        searchEmployees(event.target.value);
    });

    function searchEmployees(query) {
        countEmployees(query)

        console.log("Searching employees with query:", query);
        const xhr = new XMLHttpRequest();
        const params = new URLSearchParams({ query: query, limit: limit, offset: pageNumber - 1 });
        xhr.open("GET", "http://localhost:8080/employee/search?" + params.toString(), true);


        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) { // 4 = DONE
                if (xhr.status === 200) {
                    employees = JSON.parse(xhr.responseText);

                    // Array //
                    let container = document.getElementById('tableRow');
                    container.innerHTML = "";
                    container.innerHTML = employees.map(r => `
                    <tr attr-id="${r.id}">
                    <td><img class="image_list" src="data:image/png;base64,${r.image}"></td>
                    <td>${r.surname} ${r.name}</td>
                    <td>${r.assignements.map(a => `<div>${a.responsability.role}</div>`).join('')}</td>
                    <td>${r.assignements.map(a => `<div>${a.restaurant.name} </div>`).join('')}</td>
                    <td>${r.mail}</td>
                    <td>${r.phone}</td>
                      <td>${r.hireDate}</td>
                    <td style="display: flex; flex-wrap: nowrap">
                        <button attr-id="${r.id}" class="employeeDetails fas fa-eye" onclick="employeeDetails(event,employees)"></button>
                        <button attr-id="${r.id}" class="employeeEdit edit-btn fas fa-pen" onclick="employeeEdit(event,employees)"></button>
                        <button attr-id="${r.id}" class="employeeRemove remove-btn fas fa-trash" onclick="employeeRemove(event)"></button>
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

    /* Count employees for pagination */
    function countEmployees(query) {
        const xhr = new XMLHttpRequest();
        const params = new URLSearchParams({ query: query });
        xhr.open("GET", "http://localhost:8080/employee/count?" + params.toString(), true);
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
                            searchEmployees(document.getElementById("searchInput").value, limit, this.textContent - 1);
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
        searchEmployees("", limit, pageNumber - 1)
    }
    init();

});