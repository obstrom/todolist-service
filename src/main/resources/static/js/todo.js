document.getElementById("complete-todos-collapse-link").addEventListener('click', () => {
    const headerSpan = document.getElementById("collapse-toggle-text-show");
    const collapseShowIcon = document.getElementById("collapse-icon-show");
    const collapseHideIcon = document.getElementById("collapse-icon-hide");


    if (headerSpan.outerText === "show") {
        headerSpan.innerHTML = "hide"
        collapseHideIcon.classList.add("d-none");
        collapseShowIcon.classList.remove("d-none");
    } else {
        headerSpan.innerHTML = "show";
        collapseShowIcon.classList.add("d-none");
        collapseHideIcon.classList.remove("d-none");
    }
});

