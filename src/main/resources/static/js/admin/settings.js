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
const weeklyDate = getWeekDates();  //오늘날짜 기준으로 월 - 토까지 날짜 구하기

//해당 데이터 가져오기
//userData 에 값 있음


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
    //backgroundColor: colors[index % colors.length],
    //borderColor: borderColors[index % borderColors.length],
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




const setSocketMax = document.querySelector('.setSocketMax');
setSocketMax.addEventListener('click',function(){
    console.log('setSocketMax clicked...');
    const socketMax = document.querySelector(".socketmax").value;
    axios.get(`/admin/setSocketVal?socketMax=${socketMax}`)
    .then(
        resp=>{console.log(resp)
    })
    .catch(err=>{console.log(err);})
})

const setImageMax = document.querySelector('.setImageMax');
setImageMax.addEventListener('click',function(){
    console.log('setImageMax clicked...');
    const userImageMax = document.querySelector(".userImageMax").value;
    axios.get(`/admin/setImageUploadVal?imageMax=${userImageMax}`)
    .then(
        resp=>{console.log(resp)

    })
    .catch(err=>{console.log(err);})
})

const setMusicMax = document.querySelector('.setMusicMax');
setMusicMax.addEventListener('click',function(){
    console.log('setMusicMax clicked...');
    const userMusicMax = document.querySelector(".userMusicMax").value;
    axios.get(`/admin/setMusicUploadVal?musicMax=${userMusicMax}`)
    .then(
        resp=>{console.log(resp)
    })
    .catch(err=>{console.log(err);})
})


const setMail = document.querySelector('.setMail');
setMail.addEventListener('click',function(){
    console.log('setMail clicked...');
    const adminMail = document.querySelector(".admin-mail").value;
    const mailPassword = document.querySelector(".mail-password").value;
    axios.post(`/admin/setAdminMail?adminMail=${adminMail}&mailPassword=${mailPassword}`)
    .then(
        resp=>{console.log(resp)
    })
    .catch(err=>{console.log(err);})
})

const importUidBtn = document.querySelector('.importUidBtn');
importUidBtn.addEventListener('click',function(){
    console.log('importUidBtn clicked...');
    const importUid = document.querySelector(".import-uid").value;
    axios.post(`/admin/setPaymentVal?importUid=${importUid}`)
    .then(
        resp=>{console.log(resp)
    })
    .catch(err=>{console.log(err);})
})


