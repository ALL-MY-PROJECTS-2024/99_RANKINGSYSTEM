    var map = L.map('map').setView([36.3511658, 127.9481835], 7);

    L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        maxZoom: 18,
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, ' +
            'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        id: 'mapbox/light-v9',
        tileSize: 512,
        zoomOffset: -1
    }).addTo(map);


    // control that shows state info on hover
    var info = L.control();

    info.onAdd = function (map) {
        this._div = L.DomUtil.create('div', 'info');
        this.update();
        return this._div;
    };


    info.update = function (props) {

        this._div.innerHTML = '<div >';

        this._div.innerHTML += '<div>' + (props ?  props.name  : '') + "</div>";
        if(props){
               if(props.name.includes('강원')){
                  this._div.innerHTML += '<div>COUNT : '+result.강원도.length+'</div>';
               }
               if(props.name.includes('경기')){
                  this._div.innerHTML += '<div>COUNT : '+result.경기도.length+'</div>';
               }
               if(props.name.includes('경상남도')){
                  this._div.innerHTML += '<div>COUNT : '+result.경상남도.length+'</div>';
               }
               if(props.name.includes('경상북도')){
                  this._div.innerHTML += '<div>COUNT : '+result.경상북도.length+'</div>';
               }
               if(props.name.includes('광주광역시')){
                  this._div.innerHTML += '<div>COUNT : '+result.광주광역시.length+'</div>';
               }
               if(props.name.includes('대구광역시')){
                  this._div.innerHTML += '<div>COUNT : '+result.대구광역시.length+'</div>';
               }
               if(props.name.includes('대전광역시')){
                  this._div.innerHTML += '<div>COUNT : '+result.대전광역시.length+'</div>';
               }
               if(props.name.includes('부산광역시')){
                  this._div.innerHTML += '<div>COUNT : '+result.부산광역시.length+'</div>';
               }
               if(props.name.includes('서울특별시')){
                  this._div.innerHTML += '<div>COUNT : '+result.서울특별시.length+'</div>';
               }
               if(props.name.includes('세종특별자치시')){
                  this._div.innerHTML += '<div>COUNT : '+result.세종특별자치시.length+'</div>';
               }
               if(props.name.includes('울산광역시')){
                  this._div.innerHTML += '<div>COUNT : '+result.울산광역시.length+'</div>';
               }
               if(props.name.includes('인천광역시')){
                  this._div.innerHTML += '<div>COUNT : '+result.인천광역시.length+'</div>';
               }
               if(props.name.includes('전라남도')){
                  this._div.innerHTML += '<div>COUNT : '+result.전라남도.length+'</div>';
               }
               if(props.name.includes('제추특별자치도')){
                  this._div.innerHTML += '<div>COUNT : '+result.제추특별자치도.length+'</div>';
               }
               if(props.name.includes('충청남도')){
                  this._div.innerHTML += '<div>COUNT : '+result.충청남도.length+'</div>';
               }
               if(props.name.includes('충청북도')){
                  this._div.innerHTML += '<div>COUNT : '+result.충청북도.length+'</div>';
               }

        }


        this._div.innerHTML +='</div>'


    };

    info.addTo(map);


    // get color depending on population density value
    function getColor(d) {
        return 'lightgray';
    }

    function style(feature) {
        return {
            weight: 2, /* 경계선 굵기 변경 */
            opacity: 1,
            color: 'white',
            dashArray: '3',
            fillOpacity: 0.7,
            fillColor: getColor(feature.properties.density)
        };
    }

    function highlightFeature(e) {
        var layer = e.target;

        layer.setStyle({
            weight: 5,
            color: '#666',
            dashArray: '',
            fillOpacity: 0.7,
            fillColor : 'gray',
        });

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.bringToFront();
        }

        info.update(layer.feature.properties);
    }

    function resetHighlight(e) {
        var layer = e.target;

        if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
            layer.setStyle({
                weight: 2, /* 경계선 굵기 원래대로 변경 */
                fillColor : 'lightgray',
            });
        } else {
            layer.setStyle({
                weight: 2, /* 경계선 굵기 원래대로 변경 */
                fillColor : 'lightgray',
            });
        }

        info.update();
    }


    //클릭 이벤트 처리 함수
    function zoomToFeature(e) {

        map.fitBounds(e.target.getBounds());

        var props = e.target.feature.properties;

        const localName = document.querySelector('.localname');
        localName.innerHTML=props.name;



        const title = document.querySelector('.localname');
        title.innerHTML = (props ?  props.name  : '지역별');

        //테이블 body 내용 바꾸기
        updateTable(props.name);

        console.log(e);


    }

    function onEachFeature(feature, layer) {
        layer.on({
            mouseover:highlightFeature,
            mouseout: resetHighlight,
            click: zoomToFeature
        });
    }

    geojson = L.geoJson(statesData, {
        style: style,
        onEachFeature: onEachFeature
    }).addTo(map);

    map.attributionControl.addAttribution('Population data &copy; <a href="http://census.gov/">US Census Bureau</a>');

//--------------------------------
//--------------------------------
// Function to update table body with new data
function updateTable(localName) {
            // Get the table body element
            const tbody = document.querySelector('.table-body');

            // Clear all existing rows
            while (tbody.firstChild) {
                tbody.removeChild(tbody.firstChild);
            }
            console.log("LOCALNAME : ",localName);
            let i = 1;
            if(localName.includes('강원')){
                result.강원도.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('강원')){
                result.강원도.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('경기')){
                result.경기도.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('경상남도')){
                result.경상남도.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('경상북도')){
                result.경상북도.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('광주광역시')){
                result.광주광역시.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('대구광역시')){
                result.대구광역시.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('대전광역시')){
                result.대전광역시.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('부산광역시')){
                result.부산광역시.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('서울특별시')){
                result.서울특별시.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('세종특별자치시')){
                result.세종특별자치시.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('울산광역시')){
                result.울산광역시.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('인천광역시')){
                result.인천광역시.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('전라남도')){
                result.전라남도.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('강원')){
                result.강원도.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('제주특별자치도')){
                result.제주특별자치도.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('충청남도')){
                result.충청남도.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
            else if(localName.includes('충청북도')){
                result.충청북도.forEach(el=>{
                    createTableBody(el,i++);
                })
            }
}


function createTableBody(el,i){
                const tbody = document.querySelector('.table-body');
                const tr = document.createElement('tr');

                        const td1 = document.createElement('td');
                        const td2 = document.createElement('td');
                        const td3 = document.createElement('td');
                        const td4 = document.createElement('td');
                        const td5 = document.createElement('td');   //count
                        const td6 = document.createElement('td');   //ilikeit
                        td1.textContent = i;
                        td2.textContent = el.imagesFileInfo.images.title;
                        td3.textContent = el.imagesFileInfo.images.subCategory;
                        td4.textContent = el.imagesFileInfo.images.username;
                        td5.textContent = el.count;
                        td6.textContent = el.ilikeit;

                        tr.appendChild(td1);
                        tr.appendChild(td2);
                        tr.appendChild(td3);
                        tr.appendChild(td4);
                        tr.appendChild(td5);
                        tr.appendChild(td6);

                    tbody.appendChild(tr);
}

