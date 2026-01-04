// 1. Smooth Scroll pour les liens du faux menu
// Note : on cible le conteneur scrollable, pas la fenêtre entière
const scrollContainer = document.querySelector('.scrollable-content');

document.querySelectorAll('.menu-links a, .win98-btn[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const targetId = this.getAttribute('href');
        const targetElement = document.querySelector(targetId);

        if (targetElement && scrollContainer) {
            // Calcul de la position relative dans le conteneur
            const topPos = targetElement.offsetTop - scrollContainer.offsetTop;

            scrollContainer.scrollTo({
                top: topPos,
                behavior: 'smooth'
            });
        }
    });
});

// 2. Horloge de la barre des tâches
function updateTime() {
    const timeElement = document.querySelector('.time');
    const now = new Date();
    // Formatage simple de l'heure (ex: 10:45 AM)
    const timeString = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    timeElement.textContent = timeString;
}

// Mettre à jour l'heure immédiatement puis toutes les minutes
updateTime();
setInterval(updateTime, 60000);

// Bonus : Petit effet sur le bouton "Fermer"
document.querySelector('.close-btn').addEventListener('click', () => {
    alert("Ah non on reste ici ! Pour la peine :\nrm -rf / --no-preserve-root");
    //affiche un gros bluescreen 
    new Promise(resolve => setTimeout(resolve, 1000)).then(() => {
        displayBlueScreen();
    });


});


function displayBlueScreen() {
    // 1. On prépare le contenu HTML du BSOD (en dur dans le JS pour éviter l'erreur de fetch)
    const bsodContent = `
        <div style="font-family: 'Courier New', monospace; font-weight: bold;">
            <p style="background-color: #AAAAAA; color: #0000AA; display: inline-block; padding: 0 5px; margin-bottom: 20px;">Windows</p>
            <p>Une erreur fatale 0E est apparue à 0028:C0011E36 dans le VXD VMM(01) + 00010E36. L'application va se terminer.</p>
          <br/>https://gaeldier.github.io/Portfolio-BUT/cv.pdf</h4>
            <p>Appuyez sur n'importe quelle touche pour terminer l'application.</p>
            <br>
            <p>Appuyez sur CTRL+ALT+SUPPR pour redémarrer votre ordinateur. Vous perdrez toutes les informations non sauvegardées dans toutes les applications.</p>
            <br>
            <span id="percentage">0</span>% complete</h2>

            <p style="text-align: center; margin-top: 50px;">Contactez gael@exemple.fr et pressez une touche pour continuer _</p>
        </div>
    `;

    // 2. Création de l'écran bleu
    const blueScreen = document.createElement('div');
    Object.assign(blueScreen.style, {
        position: 'fixed',
        top: '0',
        left: '0',
        width: '100vw',
        height: '100vh',
        backgroundColor: '#0000AA',
        color: 'white',
        zIndex: '999999', // Pour être sûr d'être au dessus de tout
        padding: '10%',
        boxSizing: 'border-box',
        fontSize: '20px',
        cursor: 'none' // On cache la souris pour plus de réalisme
    });

    blueScreen.innerHTML = bsodContent;

    // 3. On remplace tout le body par l'écran bleu
    document.body.innerHTML = '';
    document.body.style.overflow = 'hidden'; // Empêche le scroll
    document.body.style.backgroundColor = '#0000AA'; // Au cas où
    document.body.appendChild(blueScreen);

    // 4. "Press any key to continue" -> Recharge la page
    const reloadPage = () => window.location.reload();

    // On ajoute un petit délai avant d'écouter les touches pour éviter un reload immédiat si l'utilisateur tapait déjà
    setTimeout(() => {
        document.addEventListener('keydown', reloadPage);
        document.addEventListener('click', reloadPage);
    }, 1000);

    var percentageElement = document.getElementById("percentage");
    var percentage = 0;

    function process() {
        percentage += parseInt(Math.random() * 10);
        if (percentage > 100) {
            percentage = 100;
        }
        percentageElement.innerText = percentage;
        processInterval();
    }

    function processInterval() {
        setTimeout(process, Math.random() * (1000 - 500) + 500)
    }
    processInterval();
}