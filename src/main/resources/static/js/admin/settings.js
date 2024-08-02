console.log('admin/settings.js')




//일별 접속 현황
const ctx = document.getElementById('totalPerDay');

const totalPerDay =
  new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: totalUser,
      datasets: [{
        label: '# of Votes',
        data: totalCountPerUser,
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
const weeklyDate = getWeekDates();  //오늘날짜 기준으로 일 - 토까지 날짜 구하기

//해당 데이터 가져오기
const userData = {
    admin: [5, 10, 15, 20, 25, 30, 35],
    user1: [10, 20, 30, 40, 50, 60, 70],
    anonymous: [2, 4, 6, 8, 10, 12, 14],
    user2: [3, 5, 7, 9, 11, 13, 15]  // 추가 유저 예시
};

// 동적 데이터셋 생성
const colors = [
    'rgba(255, 99, 132, 0.2)',
    'rgba(54, 162, 235, 0.2)',
    'rgba(75, 192, 192, 0.2)',
    'rgba(153, 102, 255, 0.2)',
    'rgba(255, 159, 64, 0.2)'
];
const borderColors = [
    'rgba(255, 99, 132, 1)',
    'rgba(54, 162, 235, 1)',
    'rgba(75, 192, 192, 1)',
    'rgba(153, 102, 255, 1)',
    'rgba(255, 159, 64, 1)'
];

const datasets = Object.keys(userData).map((username, index) => ({
    label: username,
    data: userData[username],
    backgroundColor: colors[index % colors.length],
    borderColor: borderColors[index % borderColors.length],
    borderWidth: 1
}));


const ctx2 = document.getElementById('totalPerWeek');
const totalPerWeek =
  new Chart(ctx2, {
    type: 'line',
    data: {
      labels: weeklyDate,
      datasets: datasets
    },
    options: {
      scales: {
        y: {
          beginAtZero: true
        }
      }
      ,
      plugins: {
            title: {
                     display: true,
                     text: '주간 유저 접속 현황'
                }
      }
    }
  });




//--------------------------------------------------------
// 현재 날짜를 기준으로 일 - 토 에 해당되는 날짜 구하기
//--------------------------------------------------------
function getWeekDates() {
    const today = new Date();
    const dayOfWeek = today.getDay(); // 0 (일) - 6 (토)
    const diffToMonday = today.getDate() - dayOfWeek + (dayOfWeek === 0 ? -6 : 1); // 월요일이 1번째 날

    const monday = new Date(today.setDate(diffToMonday));
    const weekDates = [];

    for (let i = 0; i < 7; i++) {
        const date = new Date(monday);
        date.setDate(monday.getDate() + i);
        weekDates.push(formatDate(date));
    }

    return weekDates;
}

function formatDate(date) {

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

console.log(getWeekDates());


