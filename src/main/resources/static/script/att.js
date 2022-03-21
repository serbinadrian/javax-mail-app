window.onload = () => {
    let content = document.getElementById('text-area');
    let imgs = content.getElementsByTagName('img');
    for (let i = 0; i < imgs.length; i++) {
        let src = imgs[i].getAttribute('src');
        if (src.includes('cid')){
            imgs[i].src = '/' + src;
        }
    }
}