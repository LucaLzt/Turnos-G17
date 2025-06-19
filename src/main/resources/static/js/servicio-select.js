document.addEventListener('DOMContentLoaded', function() {
    const lugarSelect = document.getElementById('lugar');
    const serviciosContainer = document.getElementById('servicios-checkboxes');
    const noServiciosMsg = document.getElementById('no-servicios-msg');
    const serviciosInicialesInput = document.getElementById('servicios-iniciales');
    let serviciosSeleccionados = [];
    if (serviciosInicialesInput && serviciosInicialesInput.value) {
        serviciosSeleccionados = serviciosInicialesInput.value.split(',').filter(Boolean);
    }

    function mostrarServicios(lugarId) {
        serviciosContainer.innerHTML = "";
        if (!lugarId) {
            noServiciosMsg.style.display = 'block';
            return;
        }
        fetch('/admin/profesionales/servicios-por-lugar/' + lugarId)
            .then(resp => resp.json())
            .then(servicios => {
                if (!servicios || servicios.length === 0) {
                    noServiciosMsg.style.display = 'block';
                } else {
                    noServiciosMsg.style.display = 'none';
                    let html = '';
                    servicios.forEach(servicio => {
                        const checked = serviciosSeleccionados.includes(servicio.id.toString()) ? 'checked' : '';
                        html += `<label style="margin-right:1em;">
                            <input type="checkbox" name="serviciosIds" value="${servicio.id}" ${checked}>
                            ${servicio.nombre}
                        </label>`;
                    });
                    serviciosContainer.innerHTML = html;
                }
            })
            .catch(error => {
                serviciosContainer.innerHTML = "<span style='color:red;'>Error cargando servicios</span>";
                noServiciosMsg.style.display = 'block';
                console.error("Error:", error);
            });
    }

    lugarSelect.addEventListener('change', function() {
        mostrarServicios(this.value);
    });

    // Si ya hay uno seleccionado al cargar:
    if (lugarSelect.value) {
        mostrarServicios(lugarSelect.value);
    }
});