console.log('admin/settings.js')

//일별 접속 현황
const ctx = document.getElementById('totalPerDay');
const totalPerDay =
  new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
      datasets: [{
        label: '# of Votes',
        data: [12, 19, 3, 5, 2, 3],
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });


//일별 접속 현황
const ctx2 = document.getElementById('totalPerWeek');
const totalPerWeek =
  new Chart(ctx2, {
    type: 'line',
    data: {
      labels: ['월', '화', '수', '목', '금', '토'],
      datasets: [{
        label: '# of Votes',
        data: [12, 19, 3, 5, 2, 3],
        borderWidth: 1
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });




//--------------------------------------------------------
//SWAGGER _
//--------------------------------------------------------
