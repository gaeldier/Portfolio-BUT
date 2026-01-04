// Smooth Scroll pour les liens de navigation
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();

        document.querySelector(this.getAttribute('href')).scrollIntoView({
            behavior: 'smooth'
        });
    });
});

// Animation simple d'apparition au scroll (Fade In)
const observerIntersection = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting) {
            entry.target.classList.add('visible');
        }
    });
});

// On observe toutes les sections
document.querySelectorAll('section').forEach((section) => {
    section.classList.add('hidden'); // Ajouter via CSS pour cacher par défaut
    observerIntersection.observe(section);
});
