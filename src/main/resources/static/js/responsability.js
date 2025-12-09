let limit = 10; // 10 results par page
let pageNumber = 1;

let responsability = [];

document.addEventListener("DOMContentLoaded", function () {
    /*********************************/

    document.getElementById("addButton").addEventListener("input", (event) => {
        const modal = document.getElementById("myModal-edit").style.display = "block";
    });

    / * function Handle responsability  */


    document.getElementById("searchInput").addEventListener("input", (event) => {
        pageNumber = 1; // Reset to first page on new search
        searchResponsabilitys()
    });

    document.getElementById("filterWrapper").addEventListener("change", (event) => {
        pageNumber = 1; // Reset to first page on new search
        searchResponsabilitys()
    });

    function searchResponsabilitys() {
        const filter = document.getElementById("filterWrapper").value;
        const query = document.getElementById("searchInput").value;
        const order ="asc";
        countResponsabilitys(filter, query)
        console.log("Searching responsabilitys with query:", query);
        const xhr = new XMLHttpRequest();
        const params = new URLSearchParams({ filter, filter, query: query, order:order, limit: limit, offset: pageNumber - 1 });
        xhr.open("GET", "http://localhost:8080/responsability/search?" + params.toString(), true);

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) { // 4 = DONE
                if (xhr.status === 200) {
                    responsabilitys = JSON.parse(xhr.responseText);

                    // Array //
                    let container = document.getElementById('tableRow');
                    container.innerHTML = "";
                    container.innerHTML = responsabilitys.map(r => `
                    <tr attr-id="${r.id}">
                    <td>${r.role}</td>
                    <td>
                        <button attr-id="${r.id}" class="responsabilityEdit edit-btn fas fa-pen" onclick="responsabilityEdit(event,responsabilitys)"></button>
                        <button attr-id="${r.id}" class="responsabilityRemove remove-btn fas fa-trash" onclick="responsabilityRemove(event)"></button>
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

    /* Count responsabilitys for pagination */
    function countResponsabilitys(filter, query) {
        const xhr = new XMLHttpRequest();
        const params = new URLSearchParams({ filter, filter, query: query });
        xhr.open("GET", "http://localhost:8080/responsability/count?" + params.toString(), true);
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
                            searchResponsabilitys(document.getElementById("searchInput").value, limit, this.textContent - 1);
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
        searchResponsabilitys("", limit, pageNumber - 1)
    }
    init();

});