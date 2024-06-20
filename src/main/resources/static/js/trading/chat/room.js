//----------------------------------------------------------------
//새로고침 방지코드
//----------------------------------------------------------------
function NotReload(){
    if( (event.ctrlKey == true && (event.keyCode == 78 || event.keyCode == 82)) || (event.keyCode == 116) ) {
        event.keyCode = 0;
        event.cancelBubble = true;
        event.returnValue = false;
    }
}
document.onkeydown = NotReload;

//----------------------------------------------------------------
//
//----------------------------------------------------------------
console.log("room.js...");

const memberBtn = document.querySelector('.person-group-block');
let isClosed = true
memberBtn.addEventListener('click',()=>{
    const userBlock = document.querySelector('.user-block');

    if(isClosed){
        userBlock.classList.remove('hidden');
        isClosed = false;
    }else{
        userBlock.classList.add('hidden');
        isClosed = true;
    }
})

//----------------------------------------------------------------
//낙찰확정
//----------------------------------------------------------------
//----------------------------------------------------------------
//타이머
//----------------------------------------------------------------
let timerAsync=null;
function timerFunc(seconds){
    timerAsync = setInterval(() => {
        const timerClock = document.querySelector('.timer-clock');
        timerClock.innerHTML=formatTime(seconds)
        seconds--;

        if (seconds === 0) {
            clearInterval(timerAsync);
            console.log("타이머 종료!");
            alert("경매 시간 종료..");
            timerAsync=null;
        }
    }, 1000);
}

function formatTime(seconds) {
    // 시간 계산
    let hours = Math.floor(seconds / 3600);
    let minutes = Math.floor((seconds % 3600) / 60);
    let secs = seconds % 60;

    // 시간, 분, 초를 두 자리 숫자로 포맷팅
    let hoursStr = hours.toString().padStart(2, '0');
    let minutesStr = minutes.toString().padStart(2, '0');
    let secondsStr = secs.toString().padStart(2, '0');

    return `${hoursStr}:${minutesStr}:${secondsStr}`;
}

//----------------------------------------------------------------
//사용자 계정조회
//----------------------------------------------------------------
function createUserItem(user){
                // 프로필 요소를 선택

                var userBlock = document.querySelector('.user-block');

                var profile = document.createElement('div');
                profile.classList.add('user-item');
                profile.classList.add(user);

                // 프로필 정보 설정
                var profileImageSrc = '/assets/anon.jpg';
                var usernameText = user;

                // 프로필 이미지 생성
                var profileImage = document.createElement('div');
                profileImage.classList.add('profile-image');
                profileImage.innerHTML = '<img src="' + profileImageSrc + '" alt="">';
                profile.appendChild(profileImage);

                // 사용자명 생성
                var username = document.createElement('div');
                username.classList.add('username');
                username.textContent = usernameText;
                profile.appendChild(username);

                // '낙찰' 링크 생성
                var acceptLink = document.createElement('div');
                acceptLink.classList.add('accept');
                acceptLink.innerHTML = '<a href="">낙찰</a>';
                profile.appendChild(acceptLink);

                // '1:1' 링크 생성
                var control1Link = document.createElement('div');
                control1Link.classList.add('control-1');
                control1Link.innerHTML = '<div><a href="">1:1</a></div>';
                profile.appendChild(control1Link);

                // '강퇴' 링크 생성
                var control2Link = document.createElement('div');
                control2Link.classList.add('control-2');
                control2Link.innerHTML = '<div><a href="">강퇴</a></div>';
                profile.appendChild(control2Link);


                userBlock.appendChild(profile);
}

function createUserCallResult(username,price) {
    // 부모 요소인 user-call-result-block를 선택합니다.
    var parentDiv = document.querySelector('.user-call-result-block');

    // table 요소를 생성합니다.
    var table = document.createElement('table');
    table.classList.add('table');
    table.classList.add(username);

    // thead 요소를 생성합니다.
    var thead = document.createElement('thead');
    thead.className = 'table-primary';

    // thead 내부에 tr 요소를 생성합니다.
    var trHead = document.createElement('tr');

    // 각각의 th 요소를 생성하고 내용을 설정합니다.
    var th1 = document.createElement('th');
    th1.textContent = '순위';
    var th2 = document.createElement('th');
    th2.textContent = '유저명';
    var th3 = document.createElement('th');
    th3.textContent = '호가';
    var th4 = document.createElement('th');
    th4.textContent = '선택';

    // trHead에 th 요소들을 자식으로 추가합니다.
    trHead.appendChild(th1);
    trHead.appendChild(th2);
    trHead.appendChild(th3);
    trHead.appendChild(th4);

    // thead에 trHead를 자식으로 추가합니다.
    thead.appendChild(trHead);

    // tbody 요소를 생성합니다.
    var tbody = document.createElement('tbody');

    // tbody 내부에 tr 요소를 생성합니다.
    var trBody = document.createElement('tr');

    // 각각의 td 요소를 생성하고 내용을 설정합니다.
    var td1 = document.createElement('td');
    td1.textContent = 0;
    td1.classList.add('ranking');
    var td2 = document.createElement('td');
    td2.textContent = username;
    td2.classList.add('username');
    var td3 = document.createElement('td');
    td3.textContent = price;
    td3.classList.add('price');
    var td4 = document.createElement('td');

    // td4 내에 a 요소를 생성하고 클래스 및 텍스트를 설정합니다.
    var a = document.createElement('a');
    a.href = '#';  // 링크 URL 설정 필요
    a.className = 'btn btn-secondary';
    a.textContent = '낙찰';

    // td4에 a를 자식으로 추가합니다.
    td4.appendChild(a);

    // trBody에 td 요소들을 자식으로 추가합니다.
    trBody.appendChild(td1);
    trBody.appendChild(td2);
    trBody.appendChild(td3);
    trBody.appendChild(td4);

    // tbody에 trBody를 자식으로 추가합니다.
    tbody.appendChild(trBody);

    // table에 thead와 tbody를 자식으로 추가합니다.
    table.appendChild(thead);
    table.appendChild(tbody);

    // parentDiv에 table을 자식으로 추가합니다.
    parentDiv.appendChild(table);
}