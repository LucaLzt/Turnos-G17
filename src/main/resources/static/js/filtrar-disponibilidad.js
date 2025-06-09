document.addEventListener('DOMContentLoaded', function() {
    const filtro = document.getElementById('filtroFecha');
    if (!filtro) return;
    filtro.addEventListener('change', function() {
        const seleccion = this.value; // formato yyyy-MM-dd
        const cards = document.querySelectorAll('.profesional-card');
        cards.forEach(card => {
            if (!seleccion || card.getAttribute('data-fecha') === seleccion) {
                card.style.display = '';
            } else {
                card.style.display = 'none';
            }
        });
    });
});