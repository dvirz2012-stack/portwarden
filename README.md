<div align="center">

# Portwarden: Backend

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Status](https://img.shields.io/badge/status-in%20development-orange?style=for-the-badge)

**Hi, this is Portwarden, a cool web application I'm making using Kotlin + TypeScript, here are some details:**

</div>

This program lists listening ports with their pid and process name, and kills a process by port, you can click the "Kill" button on the process you want to kill, and simple as that! :)

**How it works**

Now, the frontend is connected with the backend by an "invisible wire" which is the WebSocket port (PortWarden's port in localhost).
Frontend waits for the backend to send the codes from the JSON responses, and by that creates tables or removes a port off the table (depending on which command
was sent to the backend, FETCH_PORTS or KILL_PROCESS)
it is pretty cool, and I really enjoy making this :)

Also, the JSON response body looks like this:

```json
{ 
 "message": "Ports fetched successfully",
 "data": [{ "port": 3000, "pid": 1234, "processName": "node.exe" }],
 "type": "SUCCESS"
}
```
This is the data that gets sent from the server to the client.

**Roadmap**

I plan on making this program available to everyone, and add strong security features that will make it secure and comfortable to use.

**Notes**

 - This currently works only on Windows, the scanner uses cmd.exe, tasklist and netstat.
 - Killing system or service processes requires running as Administrator.
 - WS_PORT and WS_HOST can be set as environment variables (e.g. in IntelliJ's run config), the frontend's config.ts has the URL hardcoded to port 8887, so the two have to match.

**How to run it**

I used three jars for this:

Gson:
 converts the server's responses to JSON.

Java-WebSocket:
 Java-Websocket was used for doing actions on message, on opening of the program, and on close or if an error ever happens.

slf4j:
 required by Java-WebSocket

Steps on how to run it locally:
 1. Run Application.kt, which starts the server on ws://127.0.0.1:8887
 2. Serve the frontend folder, for example with IntelliJ's "Open in browser" or VS code live server, or npx serve.
 3. Open the page in your browser.

**Frontend repo** 

https://github.com/dvirz2012-stack/portwardenFrontend
<div align="center">

 made by **Dvir**

</div>
