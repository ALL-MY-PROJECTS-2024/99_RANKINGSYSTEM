//------------------------
// 스크롤이벤트 lodash 나중에 추가할것
//------------------------
// JavaScript 코드
window.addEventListener('scroll', function() {
    var top = document.querySelector('.top-header');
    var nav = document.querySelector('nav');
    var scrollPosition = window.scrollY;
    console.log('scroll',scrollPosition);
    // header의 초기 상태 (relative)에서 fixed로 변경하기 위한 조건
    if (scrollPosition > 30) { // 스크롤이 80px 이상 내려갔을 때
        top.classList.add('fixed-top');
        nav.classList.add('fixed-nav');
    } else { // 스크롤이 80px 이하로 올라갔을 때
        top.classList.remove('fixed-top');
        nav.classList.remove('fixed-nav');

    }
});

