document.getElementById('photoInput').addEventListener('change', function(event) {
    var image = document.getElementById('photoPreview');
    image.src = URL.createObjectURL(event.target.files[0]);
});

document.addEventListener('DOMContentLoaded', function() {
    var genderSelect = document.getElementById('gender');
    
    genderSelect.addEventListener('change', function() {
        if (genderSelect.value !== '') {
            genderSelect.classList.add('is-valid');
        }
    });
});

document.addEventListener('DOMContentLoaded', function() {
    var genderSelect = document.getElementById('workingShift');
    
    genderSelect.addEventListener('change', function() {
        if (genderSelect.value !== '') {
            genderSelect.classList.add('is-valid');
        }
    });
});