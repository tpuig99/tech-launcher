function searchFrameworks() {
    let input = document.getElementById("searchInput").value;
    console.log(input);
    let path = "/search?toSearch="+input;
    console.log(path);
    window.location.href = path;
    console.log(location.href);
}
