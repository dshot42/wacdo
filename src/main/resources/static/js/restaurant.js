let limit = 10; // 10 results par page
let pageNumber = 1;

let restaurants = [];



document.addEventListener("DOMContentLoaded", function () {



    / * function Handle restaurant  */


    /*****************************************/

    document.getElementById("searchInput").addEventListener("input", (event) => {
        pageNumber = 1; // Reset to first page on new search
        searchRestaurants();
    });
    document.getElementById("filterWrapper").addEventListener("change", (event) => {
        pageNumber = 1; // Reset to first page on new search
        searchRestaurants();
    });

    document.getElementById("withoutAssignement").addEventListener("change", (event) => {
        pageNumber = 1; // Reset to first page on new search
        searchRestaurants()
    });

    function searchRestaurants() {
        const filter = document.getElementById("filterWrapper").value;
        const query = document.getElementById("searchInput").value;
        const withoutAssignement = document.getElementById("withoutAssignement").checked;
        countRestaurants(filter, query, withoutAssignement)
        console.log("Searching restaurants with query:", query, " and filter:", filter);
        const xhr = new XMLHttpRequest();
        const params = new URLSearchParams({ filter: filter, query: query, withoutAssignement: withoutAssignement, limit: limit, offset: pageNumber - 1 });
        xhr.open("GET", /restaurant/search?" + params.toString(), true);


        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) { // 4 = DONE
                if (xhr.status === 200) {
                    restaurants = JSON.parse(xhr.responseText);

                    // Array //
                    let container = document.getElementById('tableRow');
                    container.innerHTML = "";
                    container.innerHTML = restaurants.map(r => `
                    <tr attr-id="${r.id}">
                    <td> ${r.image
                            ? `<img class="image_list" src="data:image/png;base64,${r.image}"  alt="image">`
                            : `<img class="image_list" src="/images/store2.png"  alt="Wacdo logo">`
                        }</td>
                    <td>${r.name}</td>
                     <td>
                      <div class="dropdown">
                        <button class="btn btn-color-blue dropdown-toggle"
                                type="button"
                                data-bs-toggle="dropdown"
                                aria-expanded="false">
                          ${r.assignements.length} Assignement(s)
                        </button>

                        <ul class="dropdown-menu">
                          ${r.assignements.map(a => `
                            <li><a class="dropdown-item">${a.responsability.role} => ${a.employee.name} ${a.employee.surname}</a></li>
                          `).join('')}
                        </ul>
                      </div>
                    </td>
                    <td>${r.restaurantAddress.city}</td>
                    <td>${r.restaurantAddress.address}</td>
                    <td>${r.restaurantAddress.postalCode}</td>
                    <td style="display: flex; flex-wrap: nowrap">
                        <button attr-id="${r.id}" class="restaurantDetails fas fa-eye" onclick="restaurantDetails(event,restaurants)"></button>
                        <button attr-id="${r.id}" class="restaurantEdit edit-btn fas fa-pen" onclick="restaurantEdit(event,restaurants)"></button>
                        <button attr-id="${r.id}" class="restaurantRemove remove-btn fas fa-trash" onclick="restaurantRemove(event)"></button>
                    </td>
                    </tr>
                    `).join('');

                    // MAP //
                    const markers = L.featureGroup();
                    restaurants.forEach(r => {
                        const m = L.marker([r.restaurantAddress.cordX, r.restaurantAddress.cordY]);
                        m.bindPopup(`<strong>${r.name}</strong><br>${r.restaurantAddress.address}<br> ${r.restaurantAddress.postalCode}  ${r.restaurantAddress.city}`);
                        markers.addLayer(m);
                    });
                    markers.addTo(map);

                    // Click pour ajouter un marker test
                    map.on('click', e => {
                        L.marker([e.restaurantAddress.cordX, e.restaurantAddress.cordY]).addTo(map)
                            .bindPopup(`Lat: ${e.restaurantAddress.cordX.toFixed(6)}<br>Lon: ${e.restaurantAddress.cordY.toFixed(6)}`).openPopup();
                    });

                    // Fit bounds (si au moins 1 marker)
                    if (markers.getLayers().length) {
                        map.fitBounds(markers.getBounds(), { padding: [40, 40] });
                    }

                } else {
                    console.error("Request failed with status:", xhr.status);
                }
            }
        }

        xhr.send();
    }

    /* Count restaurants for pagination */
    function countRestaurants(filter, query,withoutAssignement) {

        const xhr = new XMLHttpRequest();
        const params = new URLSearchParams({ filter: filter, query: query , withoutAssignement: withoutAssignement});
        xhr.open("GET", /restaurant/count?" + params.toString(), true);
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
                            searchRestaurants(document.getElementById("searchInput").value, limit, this.textContent - 1);
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

    // --- Création de la carte ---
    const map = L.map('map', { zoomControl: true })
    // --- Tuiles OpenStreetMap ---
    const tileLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');
    tileLayer.addTo(map);


    function init() {
        searchRestaurants("", limit, pageNumber - 1)
    }
    init();

});