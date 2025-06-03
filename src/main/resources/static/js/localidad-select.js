document.addEventListener("DOMContentLoaded", function () {
    var provinciaSelect = document.getElementById('provincia');
    var localidadSelect = document.getElementById('localidad');

    provinciaSelect.addEventListener('change', function() {
        var provinciaId = this.value;
        localidadSelect.innerHTML = '<option value="">Cargando...</option>';

        if (provinciaId) {
            fetch('/localidades/por-provincia/' + provinciaId)
                .then(response => response.json())
                .then(data => {
                    localidadSelect.innerHTML = '<option value="">Seleccione una localidad</option>';
                    data.forEach(localidad => {
                        var option = document.createElement('option');
                        option.value = localidad.id;
                        option.text = localidad.nombre;
                        localidadSelect.appendChild(option);
                    });
                })
                .catch(error => {
                    localidadSelect.innerHTML = '<option value="">Error al cargar localidades</option>';
                });
        } else {
            localidadSelect.innerHTML = '<option value="">Seleccione una localidad</option>';
        }
    });
});