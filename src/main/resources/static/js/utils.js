// Convertir la HTMLCollection en Array pour utiliser forEach
const ref = window.location.pathname
document.getElementsByClassName("nav-item-selected")[0].classList.remove("nav-item-selected");

const elements =  document.getElementsByClassName("nav-item")

if (ref == "/restaurant") {
       elements[0].classList.add("nav-item-selected");
} else if (ref == "/employee") {
   elements[1].classList.add("nav-item-selected");
}  else if (ref == "/responsability") {
    elements[2].classList.add("nav-item-selected");
}
else if (ref == "/assignment") {
    elements[3].classList.add("nav-item-selected");
}

