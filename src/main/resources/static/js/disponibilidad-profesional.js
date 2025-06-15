document.addEventListener('DOMContentLoaded', function() {
    const filtro = document.getElementById('filtroFecha');
    const limpiar = document.getElementById('limpiarFiltro');
    if (!filtro) return;
    filtro.addEventListener('change', function() {
        const seleccion = this.value; // formato yyyy-MM-dd
        const filas = document.querySelectorAll('.profesional-disponibilidad-table tbody tr[data-fecha]');
        let hayVisibles = false;
        filas.forEach(fila => {
            if (!seleccion || fila.getAttribute('data-fecha') === seleccion) {
                fila.style.display = '';
                hayVisibles = true;
            } else {
                fila.style.display = 'none';
            }
        });
        // Ocultar el mensaje de "No hay disponibilidades" si hay filas visibles
        const sinDisp = document.querySelector('.profesional-sin-disponibilidades');
        if (sinDisp) {
            sinDisp.style.display = hayVisibles ? 'none' : '';
        }
    });
    if (limpiar) {
        limpiar.addEventListener('click', function() {
            filtro.value = '';
            const filas = document.querySelectorAll('.profesional-disponibilidad-table tbody tr[data-fecha]');
            filas.forEach(fila => fila.style.display = '');
            // Mostrar el mensaje de "No hay disponibilidades" si corresponde
            const sinDisp = document.querySelector('.profesional-sin-disponibilidades');
            if (sinDisp) {
                // Si no hay filas visibles, mostrar el mensaje
                const hayFilasVisibles = Array.from(filas).some(fila => fila.style.display !== 'none');
                sinDisp.style.display = hayFilasVisibles ? 'none' : '';
            }
        });
    }
});