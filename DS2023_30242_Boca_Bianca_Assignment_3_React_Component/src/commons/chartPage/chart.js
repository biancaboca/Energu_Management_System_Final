import React, { useState, useEffect } from 'react';
import { withRouter } from 'react-router-dom';
import BackgroundImg from '../images/gray.jpg';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { Line } from 'react-chartjs-2';
import 'chartjs-adapter-date-fns';
import * as API_MONOTORING from '../monotoringDevice/monotoring-api';
import { LinearScale, PointElement, LineElement, CategoryScale, TimeScale, Tooltip, Legend } from 'chart.js';
import { Chart, registerables} from 'chart.js';

Chart.register(...registerables);
const backgroundStyle = {
  backgroundPosition: 'center',
  backgroundSize: 'cover',
  backgroundRepeat: 'no-repeat',
  width: '100vw',
  height: '100vh',
  backgroundImage: `url(${BackgroundImg})`,
};

const formContainerStyle = {
  background: 'rgba(255, 255, 255, 0.8)',
  padding: '20px',
  borderRadius: '10px',
  width: '1400px',
  margin: '0 auto',
  marginTop: '0px',
  left: '50px',
  textAlign: 'center',
  boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
};

function ChartPage(props) {
  const userId = sessionStorage.getItem('userId');
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [deviceId, setDeviceId] = useState(''); // Add an initial value for deviceId
  const [unauthorized, setUnauthorized] = useState(false);

  const [chartData, setChartData] = useState({
    labels: [],
    datasets: [{
      label: 'Energy Consumption (kWh)',
      data: [],
      borderColor: 'rgb(75, 192, 192)',
      tension: 0.1,
    }],
  });

  const chartOptions = {
    scales: {
      x: {
        type: 'linear',
        position: 'bottom',
        min: 10,
        max: 60,
        ticks: {
          stepSize: 10,
        },
        title: {
          display: true,
          text: 'Value',
        },
      },
      y: {
        type: 'linear',
        beginAtZero: true,
        title: {
          display: true,
          text: 'Energy Consumption (kWh)',
        },
      },
    },
    plugins: {
      legend: {
        position: 'top',
      },
      tooltip: {
        enabled: true,
        mode: 'index',
        intersect: false,
        callbacks: {
          label: function (context) {
            let label = context.dataset.label || '';
            if (label) {
              label += ': ';
            }
            if (context.parsed.y !== null) {
              label += context.parsed.y.toFixed(2) + ' kWh';
            }
            return label;
          },
          title: function (context) {
            if (context[0]) {
              return context[0].label;
            }
            return '';
          },
        },
      },
    },
    responsive: true,
    maintainAspectRatio: false,
  };

  useEffect(() => {
    console.log("Entering useEffect");
    setUnauthorized(true); // Reset the unauthorized state
  
    if (deviceId) {
      console.log("Device ID is present:", deviceId);
      API_MONOTORING.getOwner(deviceId, (result, status, err) => {
        console.log("getOwner API result:", result, "Status:", status, "Error:", err);
        console.log("User id" + userId);
        console.log("result" + result);
        if (result !== null && status === 200 && result === userId) {
          console.log("User is the owner");
          
          API_MONOTORING.getDate(deviceId, (dateResult, dateStatus, dateErr) => {
            if (dateResult !== null && dateStatus === 200) {
              const serverDate = new Date(dateResult);
              serverDate.setHours(0, 0, 0, 0);
  
              const selectedDateObj = new Date(selectedDate);
              selectedDateObj.setHours(0, 0, 0, 0);
  
              if (serverDate.getTime() === selectedDateObj.getTime()) {
                console.log("Selected date matches the server date");
                
                API_MONOTORING.getListMeasured((data) => {
                  console.log("getListMeasured API result:", data);
                  updateChartData(data);
                  setUnauthorized(false);
                }, deviceId);
              } else {
                console.log("Selected date does not match the server date");
                setUnauthorized(true);
                updateChartData([0, 0, 0, 0, 0, 0]);
              }
            } else {
              // Handle the error when fetching the date
              console.log("Error fetching date:", dateErr);
              setUnauthorized(true);
              updateChartData([0, 0, 0, 0, 0, 0]);
            }
          });
        } else {
          console.log("User is not the owner or API error");
          // User is not the owner or there's an API error, set unauthorized state and show default chart
          setUnauthorized(true);
          updateChartData([0, 0, 0, 0, 0, 0]);
        }
      });
    } else {
      console.log("Invalid Device ID");
      console.log("Please enter a valid Device ID.");
      setUnauthorized(true);
      updateChartData([0, 0, 0, 0, 0, 0]);
    }
  }, [deviceId, selectedDate, userId]);
  

  const updateChartData = (values) => {
    const labels = [10, 20, 30, 40, 50, 60];

    setChartData({
      labels: labels,
      datasets: [{
        label: 'Energy Consumption (kWh)',
        data: values.map((value, index) => ({ x: labels[index], y: value })),
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1,
        pointRadius: 3,
        pointBackgroundColor: 'rgb(75, 192, 192)',
      }],
    });
  };

  const handleDeviceIdChange = (event) => {
    setDeviceId(event.target.value);
  };

  return (
    <div style={backgroundStyle}>
      <div style={formContainerStyle}>
        <DatePicker
          selected={selectedDate}
          onChange={(date) => setSelectedDate(date)}
          dateFormat="yyyy/MM/dd"
        />
        <div>
          <input
            type="text"
            value={deviceId}
            onChange={handleDeviceIdChange}
            placeholder="Enter Device ID"
          />
        </div>
        {unauthorized ? (
          <div>No data available for this device.</div>
        ) : (
          <div style={{ position: 'relative', height: '40vh', width: '80vw' }}>
            <Line data={chartData} options={chartOptions} />
          </div>
        )}
      </div>
    </div>
  );
}

export default withRouter(ChartPage);
