

## 📄 Partie 5 : `05-utilisateurs.md`

### Création des utilisateurs Matrix

Une fois Synapse redémarré (`systemctl restart matrix-synapse`), vous pouvez créer vos comptes.

1. **Activation des enregistrements** :
Ajoutez ces lignes dans `homeserver.yaml` pour permettre l'usage d'interfaces comme Element :
```yaml
enable_registration: true
enable_registration_without_verification: true
registration_shared_secret: "votre_cle_secrete"

```


2. **Création manuelle via CLI** :
Utilisez l'utilitaire intégré pour créer un administrateur :
```bash
register_new_matrix_user -c /etc/matrix-synapse/homeserver.yaml http://localhost:8008
```
3. **Maintenance et Logs** :

    Logs Synapse : /var/log/matrix-synapse/homeserver.log
    Statut DB : pg_lsclusters (Vérifiez que la ligne est verte).

---

- Page précédente: [Utilisation d’une base PostgreSQL](04-psql.md)
- Page suivante: [Configuration du client Element et Reverse Proxy](../Element%20et%20Reverse%20Proxy/README.md)
