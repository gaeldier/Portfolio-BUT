// 1. Smooth Scroll pour les liens du faux menu
const scrollContainer = document.querySelector('.scrollable-content');

document.querySelectorAll('.menu-links a, .win98-btn[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const targetId = this.getAttribute('href');
        const targetElement = document.querySelector(targetId);

        if (targetElement && scrollContainer) {
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
    const timeString = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    timeElement.textContent = timeString;
}

updateTime();
setInterval(updateTime, 60000);

// Bonus : Bouton "Fermer" avec BSOD
document.querySelector('.close-btn').addEventListener('click', () => {
    if(confirm("Cette opération est illégale.\nVoulez-vous vraiment fermer Internet Explorer ? \n C'est pas super cool...")) {
        new Promise(resolve => setTimeout(resolve, 3000)).then(() => {
            displayBlueScreen();
        });
    }
});

function displayBlueScreen() {
    const bsodContent = `
        <div style="font-family: 'Courier New', monospace; font-weight: bold;">
            <p style="background-color: #AAAAAA; color: #0000AA; display: inline-block; padding: 0 5px; margin-bottom: 20px;">Windows</p>
            <p>Une erreur fatale 0E est apparue à 0028:C0011E36 dans le VXD VMM(01) + 00010E36. L'application va se terminer.</p>
            <br/>
            <p>Appuyez sur n'importe quelle touche pour terminer l'application.</p>
            <br>
            <p>Appuyez sur CTRL+ALT+SUPPR pour redémarrer votre ordinateur.</p>
            <br>
            <h2 style="text-align:center"><span id="percentage">0</span>% complete</h2>
            <p style="text-align: center; margin-top: 50px;">Contactez gaeldierynck@gmail.com et pressez une touche pour continuer _</p>
        </div>
    `;

    const blueScreen = document.createElement('div');
    Object.assign(blueScreen.style, {
        position: 'fixed', top: '0', left: '0', width: '100vw', height: '100vh',
        backgroundColor: '#0000AA', color: 'white', zIndex: '999999',
        padding: '10%', boxSizing: 'border-box', fontSize: '20px', cursor: 'none'
    });

    blueScreen.innerHTML = bsodContent;

    document.body.innerHTML = '';
    document.body.style.overflow = 'hidden';
    document.body.style.backgroundColor = '#0000AA';
    document.body.appendChild(blueScreen);

    const reloadPage = () => window.location.reload();

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
        if (percentageElement) percentageElement.innerText = percentage;
        processInterval();
    }

    function processInterval() {
        if(percentage < 100) setTimeout(process, Math.random() * (1000 - 500) + 500);
    }
    processInterval();
}