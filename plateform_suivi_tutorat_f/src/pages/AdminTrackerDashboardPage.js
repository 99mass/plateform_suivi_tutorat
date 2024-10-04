// src/pages/AdminDasboardPage.js
import React, { useEffect, useState } from 'react';
import '../styles/main.css';

// components
import NavbarLeft from '../components/NavbarLeft';
import ResponsiveAppBar from '../components/ResponsiveAppBar';
import BlocCadre from '../components/dashboard/BlocCadre';
import UserRegistrationForm from '../components/users/UserRegistrationForm';
import UserDataTable from '../components/users/UserDataTable';
import ModuleCardList from '../components/modules/ModuleCardList';
import GroupCardList from '../components/groupes/GroupCardList';
import SessionManagement from '../components/seances/SessionManagement';
import ReportsInterface from '../components/rapports/ReportsInterface';
import sessionService from '../services/sessionService';

const AdminTrackerDashboardPage = () => {
  const [activeComponent, setActiveComponent] = useState('dashboard');
  const [userType, setUserType] = useState('');
  const [isNavbarExpanded, setIsNavbarExpanded] = useState(true);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const userInformation = async () => {
      const infos = await sessionService.userInformation();
      setUser(infos);
    };
    userInformation();
  }, []);

  const handleDashboardClick = () => {
    setActiveComponent('dashboard');
  };

  const handleAddUserClick = () => {
    setActiveComponent('addUser');
  };

  const handleUserTypeClick = (type) => {
    setActiveComponent('userTable');
    setUserType(type);
  };

  const handleManageModulesClick = () => {
    setActiveComponent('manageModules');
  };

  const handleManageGroupsClick = () => {
    setActiveComponent('manageGroups');
  };

  const handleManageSessionsClick = () => {
    setActiveComponent('manageSessions');
  };
  const handleReportsClick = () => {
    setActiveComponent('reports');
  }

  const handleNavbarToggle = (expanded) => {
    setIsNavbarExpanded(expanded);
  };

  return (
    <div className={`main-page ${(activeComponent === 'addUser' || activeComponent === 'manageModules' || activeComponent === 'manageGroups') && "main-page-profile"}`}>
      <ResponsiveAppBar user={user} />
      <div className='main-body'>
        <NavbarLeft
          onDashboardClick={handleDashboardClick}
          onAddUserClick={handleAddUserClick}
          onUserTypeClick={handleUserTypeClick}
          onManageModulesClick={handleManageModulesClick}
          onManageGroupsClick={handleManageGroupsClick}
          onManageSessionsClick={handleManageSessionsClick}
          onReportsClick={handleReportsClick}
          onToggle={handleNavbarToggle}
          userData={user}
        />
        <div className={`body-page ${isNavbarExpanded ? '' : 'expanded'}`}>
          {activeComponent === 'dashboard' && <BlocCadre />}
          {activeComponent === 'addUser' && <UserRegistrationForm userData={user} />}
          {activeComponent === 'userTable' && <UserDataTable userType={userType} userData={user} />}
          {activeComponent === 'manageModules' && <ModuleCardList />}
          {activeComponent === 'manageGroups' && <GroupCardList />}
          {activeComponent === 'manageSessions' && <SessionManagement />}
          {activeComponent === 'reports' && <ReportsInterface />}
        </div>
      </div>
    </div>
  );
};

export default AdminTrackerDashboardPage;