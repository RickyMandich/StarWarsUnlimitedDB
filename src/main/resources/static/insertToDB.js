function filtra() {
    let filtro = document.getElementById("filtroTratto").value;
    output(filtro)
    let tratti = document.getElementsByClassName("tratto");
    for(let i=0;i<tratti.length;i++){
        tratti[i].style.display = "inline-flex";
        output(tratti[i].innerHTML);
        if(!tratti[i].innerHTML.includes(filtro)){
            tratti[i].style.display = "none";
        }/**/
    }
    /*
    tratti[0].toString()
    output(tratti[0].innerHTML);
    console.log(tratti[0]);
    */
}

function toggleVisibilityInput(id){
    if(document.getElementById(id).checked){
        document.getElementById("valore"+id).style.display = "inline-block";
    }else{
        document.getElementById("valore"+id).style.display = "none";

    }
}

function toggleVisibilityArena() {
    console.log("Toggle visibility called");
    let tipo = document.getElementById("selectTipo").value;
    let arenaElement = document.getElementById("arena");

    if (arenaElement) {
        if (tipo === "unità") {
            arenaElement.style.display = "block";
        } else {
            arenaElement.style.display = "none";
        }
    } else {
        console.error("Element with id 'arena' not found");
    }
}

document.addEventListener("DOMContentLoaded", function() {
    let selectTipo = document.getElementById("selectTipo");
    if (selectTipo) {
        selectTipo.addEventListener("change", toggleVisibilityArena);
    } else {
        console.error("Element with id 'selectTipo' not found");
    }
});

addEventListener("DOMContentLoaded", (event)=>{
    let check = document.getElementsByTagName("input:checkbox")
});

addEventListener("DOMContentLoaded", (event) => {
    let tratti = Array.from(document.getElementsByClassName("tratto"));
    let maxHeight = Math.max(...tratti.map(el => el.offsetHeight));

    tratti.forEach(tratto => {
        tratto.style.height = `${maxHeight}px`;
    });
});

function output(out){
    out = out + "\n";
    console.log(out);
}

document.addEventListener("DOMContentLoaded", function() {
    let input = document.getElementById("filtroTratto");
    if (input) {
        input.addEventListener("keyup", function(event) {
            if (event.key === "Enter") {
                event.preventDefault();
                let filtroButton = document.getElementById("launchFiltro");
                if (filtroButton) {
                    filtroButton.click();
                } else {
                    console.error("Pulsante di filtro non trovato");
                }
            }
        });
    } else {
        console.error("Elemento di input del filtro non trovato");
    }
});