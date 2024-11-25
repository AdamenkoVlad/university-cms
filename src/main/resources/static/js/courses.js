const createButton = document.getElementsByClassName("create-button")[0];
const updateButtons = document.getElementsByClassName("update-button");
const deleteButtons = document.getElementsByClassName("delete-button");

createButton.addEventListener('click', function onClick() {
	createButton.style.backgroundColor = '#ccfccc';
	createButton.style.border = '2px solid lightgreen';

	setTimeout(() => {
		createButton.style.backgroundColor = 'lightgreen';
		createButton.style.border = '2px solid limegreen';
	}, 1000);
});

for (let i = 0; i < updateButtons.length; i++) {
	updateButtons[i].addEventListener('click', function onClick() {
		updateButtons[i].style.backgroundColor = '#ccf7fc';
		updateButtons[i].style.border = '2px solid #76bcf5';

		setTimeout(() => {
			updateButtons[i].style.backgroundColor = '#76bcf5';
			updateButtons[i].style.border = '2px solid #05d3f7';
		}, 1000);
	});
}

for (let i = 0; i < deleteButtons.length; i++) {
	deleteButtons[i].addEventListener('click', function onClick() {
		deleteButtons[i].style.backgroundColor = '#fcebef';
		deleteButtons[i].style.border = '2px solid #f57691';

		setTimeout(() => {
			deleteButtons[i].style.backgroundColor = '#f57691';
			deleteButtons[i].style.border = '2px solid #f70505';
		}, 1000);
	});
}

const searchInput = document.getElementById('searchInput');
const dropdownItems = document.querySelectorAll('.dropdown-item');
var selectedOptionId;

dropdownItems.forEach(item => {
	item.addEventListener('click', function() {
		const type = this.getAttribute('data-type');
		searchInput.type = type;
		selectedOptionId = this.getAttribute('id');
	});
});

function setFormAction(value) {
	document.getElementById('searchForm').action = value;
}


document.getElementById('searchForm').addEventListener('submit', function() {
	const searchValue = document.getElementById('searchInput').value;
	document.getElementById('idLink').href = '/courses/' + (searchValue || 0) + '/findById';
	document.getElementById('courseNameLink').href = '/courses/' + (searchValue || " ") + '/findByCourseName';
	document.getElementById('specializationLink').href = '/courses/' + (searchValue || " ") + '/findAllBySpecialization';
	document.getElementById('teacherIdLink').href = '/courses/' + (searchValue || " ") + '/findAllByTeacherId ';
	document.getElementById('teacherNameLink').href = '/courses/' + (searchValue || " ") + '/findAllByTeacherName';
	document.getElementById('roomIdLink').href = '/courses/' + (searchValue || " ") + '/findAllByRoomId';
	document.getElementById('roomNumberLink').href = '/courses/' + (searchValue || " ") + '/findAllByRoomNumber';

	var linkElement = document.getElementById(selectedOptionId);
	var hrefValue = linkElement.getAttribute('href');
	setFormAction(hrefValue);

});

