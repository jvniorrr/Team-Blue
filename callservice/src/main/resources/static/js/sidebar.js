var menu_btn = document.querySelector("#menu-btn");
var sidebar = document.querySelector("#sidebar");
var container = document.querySelector(".my-container");
var icon = document.querySelector('.fa-chevron-right')

menu_btn.addEventListener("click", () => {
    sidebar.classList.toggle("active-nav");
    menu_btn.classList.toggle("active-cont");
    icon.classList.toggle("fa-chevron-right");
    icon.classList.toggle("fa-chevron-left");
    // container.classList.toggle("active-cont");
});