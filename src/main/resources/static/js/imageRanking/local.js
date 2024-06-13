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

//        this._div.innerHTML = '<div>';
//        this._div.innerHTML += (props ?  props.name  : '');
//        this._div.innerHTML +='</div>'
        const title = document.querySelector('.localname');
        title.innerHTML = (props ?  props.name  : '지역별');
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

    function zoomToFeature(e) {
        map.fitBounds(e.target.getBounds());
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