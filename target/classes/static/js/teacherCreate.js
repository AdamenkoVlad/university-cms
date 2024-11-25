document.addEventListener('DOMContentLoaded', function() {
	document.getElementById('file').addEventListener('change', function() {
		var fileInput = document.getElementById('file');
		var file = fileInput.files[0];

		if (file) {
			var formData = new FormData();
			formData.append('file', file);
			var csrfToken = document.getElementById("csrf").value;
			formData.append('_csrf', csrfToken);
			var xhr = new XMLHttpRequest();
			xhr.open('POST', '/avatars?role=TEACHER', true);

			xhr.onload = function() {
				if (xhr.status >= 200 && xhr.status < 300) {
					var contentType = xhr.getResponseHeader('Content-Type');
					if (contentType && contentType.includes('text/html')) {
						document.open();
						document.write(xhr.responseText);
						document.close();
					} else {
						var response = JSON.parse(xhr.responseText);
						let imageUrl = "/avatars/" + response.id;
						document.getElementById('photoId').value = response.id;
						document.getElementById('imagePreview').src = imageUrl;
					}
				}
			};
			xhr.send(formData);
		}
	});
});

document.addEventListener('DOMContentLoaded', function() {
	var selectElement = document.getElementById("gender");
	if (window.gender !== null) {
		selectElement.classList.add('is-valid');
	}
});

document.addEventListener('DOMContentLoaded', function() {
	var selectElement = document.getElementById("workingShift");
	if (window.workingShift !== null) {
		selectElement.classList.add('is-valid');
	}
});
