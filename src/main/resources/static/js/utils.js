// Convertir la HTMLCollection en Array pour utiliser forEach
const navHandler = () => {
    const ref = window.location.pathname
    document.getElementsByClassName("nav-item-selected")[0].classList.remove("nav-item-selected");

    const elements = document.getElementsByClassName("nav-item")

    if (ref == "/restaurant") {
        elements[0].classList.add("nav-item-selected");
        document.getElementById("withoutAssignementBlock").style.display = "flex"
    } else if (ref == "/employee") {
        elements[1].classList.add("nav-item-selected");
        document.getElementById("withoutAssignementBlock").style.display = "flex"
    } else if (ref == "/responsability") {
        elements[2].classList.add("nav-item-selected");
        document.getElementById("withoutAssignementBlock").style.display = "none"
    }
    else if (ref == "/assignement") {
        elements[3].classList.add("nav-item-selected");
        document.getElementById("withoutAssignementBlock").style.display = "none"

    }
}
navHandler();


function displayDate(localDate) {
    const dateObj = new Date(localDate);
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return dateObj.toLocaleDateString('fr-FR', options);
}

function phoneFormatter(phone) {
    return phone.replace(/(\d{2})(?=\d)/g, '$1 ');
}

/* search  drop down handling */

function dropDownHandler() {
    function setupDropdown(inputId, dropdownId, hiddenInputSelector, eventType) {
        const input = document.getElementById(inputId);
        const dropdown = document.getElementById(dropdownId);
        const hiddenInput = document.querySelector(hiddenInputSelector);

        if (!input || !dropdown) return;

        // Filtrer les options
        input.addEventListener(eventType, function () {
            const filter = input.value.toLowerCase();
            const items = dropdown.querySelectorAll('.dropdown-item');
            let hasVisible = false;

            items.forEach(item => {
                if (item.textContent.toLowerCase().includes(filter)) {
                    item.style.display = 'block';
                    hasVisible = true;
                } else {
                    item.style.display = 'none';
                }
            });

            // Afficher le dropdown seulement si il y a des résultats
            dropdown.style.display = hasVisible ? 'block' : 'none';
        });

        // Remplir l'input au clic sur un item
        dropdown.addEventListener('click', function (e) {
            const li = e.target.closest('.dropdown-item');
            if (li) {
                input.value = li.textContent;
                hiddenInput.value = li.getAttribute('data-id');
                dropdown.style.display = 'none';
            }
        });

        // Masquer si clic en dehors
        document.addEventListener('click', function (e) {
            if (!input.contains(e.target) && !dropdown.contains(e.target)) {
                dropdown.style.display = 'none';
            }
        });
    }

    if (document.getElementById('searchRestaurantInput')) {
        setupDropdown('searchRestaurantInput', 'searchRestaurantDropdown', 'input[name="id.restaurantId"]', 'input');
        setupDropdown('searchRestaurantInput', 'searchRestaurantDropdown', 'input[name="id.restaurantId"]', 'click');
    }
    if (document.getElementById('searchEmployeeInput')) {
        setupDropdown('searchEmployeeInput', 'searchEmployeeDropdown', 'input[name="id.employeeId"]', 'input');
        setupDropdown('searchEmployeeInput', 'searchEmployeeDropdown', 'input[name="id.employeeId"]', 'click');
    }
}

dropDownHandler();
