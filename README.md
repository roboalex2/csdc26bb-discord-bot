# csdc26bb-discord-bot

Aktuelle features:

### Commands:

##### Nur für Gilden-Administrator:

`/reminder-manager add <role>` -> Gibt einer Rolle die Rechte reminder anzulegen/löschen.

`/reminder-manager remove <role>` -> Entfernt die Rechte zum Anlegen von Remindern.

`/reminder-manager list` -> Du kannst es dir denken.

##### Für jeden der eine Reminder-Manager Rolle hat:

`/reminder create <receiverRole> <timestamp> <titel> [description]`:

- reveiverRole -> Jene Rolle die diesen Reminder erhalten soll. Auch @everyone möglich.
  
- timestamp -> Der Zeitstempel zu dem der Reminder verschickt wird. Im ISO Format: `2023-10-05T11:56:20Z` , `2023-10-05T11:56:20+02:00` Die Zeitzone beachten. Wir sind +2
  
- titel -> Der Titel des Reminders. Sollte kurz angeben worum es geht.
  
- description -> (optional) Eine genauere erklärung des Reminders bsp mit Zoom link oder erklärung der Aufgabe.
  

`/reminder abort <id>` -> Abbrechen eines Reminders. Die ID erhält man beim erstellen oder durch `/reminder list`

`/reminder list` -> Gibt dir eine Liste aller offenen Reminder.
