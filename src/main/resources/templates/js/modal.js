    const modal = document.getElementById("myModal");
    const openBtn = document.getElementById("openModalBtn");
    const closeBtn = document.getElementById("modal-close");

    // Ouvrir la modale
    openBtn.addEventListener("click", () => {
      modal.style.display = "block";
    });

    // Fermer la modale en cliquant sur le bouton X
    closeBtn.addEventListener("click", () => {
      modal.style.display = "none";
    });

    // Fermer la modale en cliquant en dehors du contenu
    window.addEventListener("click", (e) => {
      if (e.target === modal) {
        modal.style.display = "none";
      }
    });