// src/pages/TuteurDashboardPage.js
import React, { useEffect, useState } from 'react';
import '../styles/main.css';

// components
import ResponsiveAppBar from '../components/ResponsiveAppBar';
import Dashboard from '../components/dashboard/Tuteur';
import sessionService from '../services/sessionService';

const TuteurDashboardPage = () => {

  const [user, setUser] = useState(null);

  useEffect(() => {
    const userInformation = async () => {
      const infos = await sessionService.userInformation();
      setUser(infos);
    };
    userInformation();
  }, []);

  return (
    <div className="main-page">
      <ResponsiveAppBar user={user} />
      <div className='main-tuteur-dasboard'>
        <Dashboard user={user} tuteurId={user && user.id} />
      </div>
    </div>
  );
};

export default TuteurDashboardPage;
