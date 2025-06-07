document.addEventListener('DOMContentLoaded', function() {
    const lugarSelect = document.getElementById('lugar');
    const serviciosSelect = document.getElementById('servicios');
    const guardarBtn = document.querySelector('.btn-guardar');
    const formGroupServicios = serviciosSelect.closest('.form-group');
    let noServiciosMsg = document.getElementById('no-servicios-msg');

    // Crear el mensaje si no existe
    if (!noServiciosMsg) {
        noServiciosMsg = document.createElement('div');
        noServiciosMsg.id = 'no-servicios-msg';
        noServiciosMsg.className = 'alert alert-warning';
        noServiciosMsg.style.display = 'none';
        noServiciosMsg.textContent = 'Este lugar no tiene servicios asociados. Debe asociar al menos uno antes de continuar.';
        formGroupServicios.appendChild(noServiciosMsg);
    }

    function toggleGuardarBtnAndMessage() {
        const hasOptions = serviciosSelect.options.length > 0;
        guardarBtn.disabled = !hasOptions;
        noServiciosMsg.style.display = hasOptions ? 'none' : 'block';
    }

    lugarSelect.addEventListener('change', function() {
        const lugarId = this.value;
        serviciosSelect.innerHTML = '';
        if (lugarId) {
            fetch(`/profesionales/servicios-por-lugar/${lugarId}`)
                .then(response => response.json())
                .then(servicios => {
                    servicios.forEach(serv => {
                        const option = document.createElement('option');
                        option.value = serv.id;
                        option.textContent = serv.nombre;
                        serviciosSelect.appendChild(option);
                    });
                    toggleGuardarBtnAndMessage();
                });
        } else {
            toggleGuardarBtnAndMessage();
        }
    });

    // Llama al inicio por si ya hay lugar cargado
    toggleGuardarBtnAndMessage();
});