window.onload = () => {
    //menu
    let inboxSwitch = document.getElementById('inbox-switch');
    let sentSwitch = document.getElementById('sent-switch');
    //menu content
    let inboxContent = document.getElementById('inbox');
    let sentContent = document.getElementById('sent');
    //display controls
    let displayAll = document.getElementById('display-all');
    let displayRead = document.getElementById('display-read');
    let displayUnread = document.getElementById('display-unread');
    //memory
    let currentMenuItem = inboxSwitch;
    let currentDisplayControlItem = displayAll;
    //messages
    let messages = inboxContent.getElementsByClassName('message');
    let sentMessages = sentContent.getElementsByClassName('message');
    let allMessages = [...messages, ...sentMessages];
    //mail control
    let compose = document.getElementById('compose');
    let sendEmailWindow = document.getElementById('send-modal');
    let lookEmailWindow = document.getElementById('look-modal');

    let sendEmail = document.getElementById('send-email');
    let emailInput = document.getElementById('email-input');

    let closeWriteEmail = document.getElementById('close-write-email');
    let closeLookEmail = document.getElementById('close-look-email');

    let mailToArea = document.getElementById('mail-to-area');
    let subjectArea = document.getElementById('subject-area');
    let textArea = document.getElementById('text-area');
    //flags
    let unreadFlag = false;
    let sentFlag = false;

    inboxSwitch.addEventListener('click', () => {
        sentFlag = false;
        currentMenuItem.classList.remove('active');
        inboxSwitch.classList.add('active');
        currentMenuItem = inboxSwitch;
        sentContent.classList.remove('show');
        sentContent.classList.add('hide');
        inboxContent.classList.remove('hide');
        inboxContent.classList.add('show');
    });

    sentSwitch.addEventListener('click', () => {
        sentFlag = true;
        currentMenuItem.classList.remove('active');
        sentSwitch.classList.add('active');
        currentMenuItem = sentSwitch;
        inboxContent.classList.remove('show');
        inboxContent.classList.add('hide');
        sentContent.classList.remove('hide');
        sentContent.classList.add('show');
    });

    displayAll.addEventListener('click', () =>{
        currentDisplayControlItem.classList.remove('active');
        displayAll.classList.add('active');
        currentDisplayControlItem = displayAll;
        Array.prototype.forEach.call(messages,message => {
            message.classList.remove('hide');
            message.classList.add('show');
        });
    });

    displayRead.addEventListener('click', () =>{
        unreadFlag = false;
        currentDisplayControlItem.classList.remove('active');
        displayRead.classList.add('active');
        currentDisplayControlItem = displayRead;
        Array.prototype.forEach.call(messages,message => {
            if (message.classList.contains('read')) {
                message.classList.remove('hide');
                message.classList.add('show');
            }
            else {
                message.classList.add('hide');
            };
        });
    });

    displayUnread.addEventListener('click', () =>{
        unreadFlag = true;
        currentDisplayControlItem.classList.remove('active');
        displayUnread.classList.add('active');
        currentDisplayControlItem = displayUnread;
        Array.prototype.forEach.call(messages,message => {
            if (message.classList.contains('unread')) {
                message.classList.remove('hide');
                message.classList.add('show');
            }
            else {
                message.classList.add('hide');
            };
        });
    });

    compose.addEventListener('click', (e) => {
        e.preventDefault();
        sendEmailWindow.classList.remove('hide');
        sendEmailWindow.classList.add('show');
    });

    closeWriteEmail.addEventListener('click', (e) => {
        e.preventDefault();
        sendEmailWindow.classList.remove('show');
        sendEmailWindow.classList.add('hide');
    });

    closeLookEmail.addEventListener('click', (e) => {
        e.preventDefault();
        lookEmailWindow.classList.remove('show');
        lookEmailWindow.classList.add('hide');
    });

    sendEmail.disabled = sendEmail.value.length < 1
    emailInput.addEventListener('input', (e) => {
        sendEmail.disabled = e.target.value.length < 1;
    });

    Array.prototype.forEach.call(allMessages,message => {
        message.addEventListener('click', () => {
            message.classList.remove('unread');
            message.classList.add('read');
            mailToArea.innerHTML = message.getElementsByClassName('from')[0].innerHTML;
            subjectArea.innerHTML = message.getElementsByClassName('subject')[0].innerHTML;
            textArea.innerHTML = message.getElementsByClassName('attachments')[0].innerHTML;
            lookEmailWindow.classList.remove('hide');
            lookEmailWindow.classList.add('show');

            if(unreadFlag && !sentFlag) {
                message.classList.add('hide');
            }
        });
    });

}