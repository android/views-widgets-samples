const myHeading = document.querySelector('h1');
myHeading.textContent = 'Hello world!';

let nativePort= null;


window.addEventListener('message', message => {
//check if the data exists if so try to parse it
    if (e.data) {
        val msg = JSON.parse(e.data);
        //if it is in init message then we know it contains the info we need to send a message back
        if msg (msg.type === 'init'){
            nativePort= e.ports[0];
        } else {
            onNativeMessage(msg);
        }

      }

    });

function sendMessageToNative(data) {
nativePort.postMessage(JSON.stringify(data));
}