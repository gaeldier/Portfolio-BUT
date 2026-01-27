

## 📄 Partie 2 : `02-acces-distant.md`

### Accès au service depuis la machine physique

La VM étant sur un réseau privé (`10.42.0.0/16`), elle n'est pas directement accessible. Nous utilisons un tunnel SSH.

1. **Tunnel ponctuel** :
```bash
ssh -L 9090:localhost:80 user@10.42.xx.1

```


2. **Configuration permanente** (sur votre machine physique dans `~/.ssh/config`) :
```text
Host vm
    HostName 10.42.xx.1
    User user
    LocalForward 9090 localhost:80   # Pour tester Nginx
    LocalForward 8008 localhost:8008 # Pour Synapse

```

*Note : Une fois configuré, tapez simplement `ssh vm` pour ouvrir le tunnel.*

---

- Page précédente: [Mise en place d’un service HTTP de test (nginx)](01-service-test-nginx.md)
- Page suivante: [Installation de Synapse](03-installation-synapse.md)
