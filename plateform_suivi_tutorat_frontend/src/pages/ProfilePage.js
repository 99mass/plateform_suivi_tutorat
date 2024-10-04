// src/pages/ProfilePage.js
import React, { useEffect, useState } from 'react';
import '../styles/main.css';
import ResponsiveAppBar from '../components/ResponsiveAppBar';
import UserProfile from '../components/users/UserProfile';
import NavbarLeft from '../components/NavbarLeft';
import BlocCadre from '../components/dashboard/BlocCadre';
import UserRegistrationForm from '../components/users/UserRegistrationForm';
import UserDataTable from '../components/users/UserDataTable';
import ModuleCardList from '../components/modules/ModuleCardList';
import GroupCardList from '../components/groupes/GroupCardList';
import SessionManagement from '../components/seances/SessionManagement';
import ReportsInterface from '../components/rapports/ReportsInterface';
import sessionService from '../services/sessionService';

const ProfilePage = () => {
    const [activeComponent, setActiveComponent] = useState('dashboard');
    const [userType, setUserType] = useState('');
    const [isNavbarExpanded, setIsNavbarExpanded] = useState(true);
    const [showProfile, setShowProfile] = useState(false);
    const [currentPath, setCurrentPath] = useState('');
    const [userRole, setUserRole] = useState('');
    const [user, setUser] = useState(null);



    useEffect(() => {
        const path = window.location.pathname;
        setCurrentPath(path);

        const userInformation = async () => {
            const infos = await sessionService.userInformation();
            setUser(infos);
            setUserRole(infos.role);
        };

        userInformation();
    }, []);

    const handleDashboardClick = () => {
        setCurrentPath("")
        handlePath();
        setActiveComponent('dashboard');
        setShowProfile(false);
    };

    const handleAddUserClick = () => {
        setCurrentPath("")
        handlePath();
        setActiveComponent('addUser');
        setShowProfile(false);
    };

    const handleUserTypeClick = (type) => {
        setCurrentPath("")
        handlePath();
        setActiveComponent('userTable');
        setUserType(type);
        setShowProfile(false);
    };

    const handleManageModulesClick = () => {
        setCurrentPath("")
        handlePath();
        setActiveComponent('manageModules');
        setShowProfile(false);
    };

    const handleManageGroupsClick = () => {
        setCurrentPath("")
        handlePath();
        setActiveComponent('manageGroups');
        setShowProfile(false);
    };

    const handleManageSessionsClick = () => {
        setCurrentPath("")
        handlePath();
        setActiveComponent('manageSessions');
        setShowProfile(false);
    };

    const handleReportsClick = () => {
        setCurrentPath("")
        handlePath();
        setActiveComponent('reports');
        setShowProfile(false);
    };

    const handleNavbarToggle = (expanded) => {
        setIsNavbarExpanded(expanded);
    };

    const handleProfileClick = () => {
        setShowProfile(true);
        setActiveComponent('profile');
    };

    const handlePath = () => {
         if (userRole==="tuteur") {
            window.location.pathname="tuteur";
         }else{
            window.location.pathname="dasboard";
         }
    }

    return (
        <div className='main-page main-page-profile' >
            <ResponsiveAppBar user={user} onProfileClick={handleProfileClick}  handlePath={handlePath}/>
            {userRole !== "tuteur" && <div className='main-body' >
                <NavbarLeft
                    onDashboardClick={handleDashboardClick}
                    onAddUserClick={handleAddUserClick}
                    onUserTypeClick={handleUserTypeClick}
                    onManageModulesClick={handleManageModulesClick}
                    onManageGroupsClick={handleManageGroupsClick}
                    onManageSessionsClick={handleManageSessionsClick}
                    onReportsClick={handleReportsClick}
                    onToggle={handleNavbarToggle}
                />
                <div className={`body-page ${isNavbarExpanded ? '' : 'expanded'}`}
                >
                    {showProfile || currentPath === "/profile" ? (
                        <UserProfile userData={user} />
                    ) : (
                        <>
                            {activeComponent === 'dashboard' && <BlocCadre />}
                            {activeComponent === 'addUser' && <UserRegistrationForm />}
                            {activeComponent === 'userTable' && <UserDataTable userType={userType} />}
                            {activeComponent === 'manageModules' && <ModuleCardList />}
                            {activeComponent === 'manageGroups' && <GroupCardList />}
                            {activeComponent === 'manageSessions' && <SessionManagement />}
                            {activeComponent === 'reports' && <ReportsInterface />}
                        </>
                    )}
                </div>
            </div>
            }
            {userRole === "tuteur" &&
                <div className='main-tuteur-profile'>
                    <UserProfile userData={user} />
                </div>
            }
        </div>
    );
};

export default ProfilePage;