import React, { useCallback, useState } from 'react';
import {
    Paper,
    List,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Collapse,
    Divider,
    Box,
    Typography,
    IconButton,
    Tooltip
} from '@mui/material';
import {
    Dashboard,
    TrackChanges,
    School,
    Assessment,
    People,
    Class,
    Group,
    ExpandLess,
    ExpandMore,
    PersonAdd,
    Settings,
    AdminPanelSettings,
    SupervisorAccount,
    Logout,
    ChevronLeft,
    ChevronRight
} from '@mui/icons-material';
import { useTheme } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';
import sessionService from '../services/sessionService';

const NavbarLeft = ({ onDashboardClick, onAddUserClick, onUserTypeClick, onManageModulesClick, onManageGroupsClick, onManageSessionsClick, onReportsClick, onToggle, userData }) => {
    const theme = useTheme();
    const navigate = useNavigate();
    const [openUsers, setOpenUsers] = useState(false);
    const [openModules, setOpenModules] = useState(false);
    const [openGroups, setOpenGroups] = useState(false);
    const [openSessions, setOpenSessions] = useState(false);
    const [isExpanded, setIsExpanded] = useState(true);

    const handleLogout = useCallback(() => {
        sessionService.endSession();
        navigate('/');
    }, [navigate]);

    const handleUsersClick = () => {
        setOpenUsers(!openUsers);
    };

    const handleModulesClick = () => {
        setOpenModules(!openModules);
    };

    const handleGroupsClick = () => {
        setOpenGroups(!openGroups);
    };

    const handleSessionsClick = () => {
        setOpenSessions(!openSessions);
    };

    const toggleNavbar = () => {
        setIsExpanded(!isExpanded);
        onToggle(!isExpanded)
    };

    const renderListItem = (icon, text, onClick, subItems = null) => (
        <>
            <ListItemButton onClick={onClick}>
                <Tooltip title={isExpanded ? '' : text} placement="right">
                    <ListItemIcon>
                        {icon}
                    </ListItemIcon>
                </Tooltip>
                {isExpanded && <ListItemText primary={text} />}
                {isExpanded && subItems && (subItems.open ? <ExpandLess /> : <ExpandMore />)}
            </ListItemButton>
            {subItems && (
                <Collapse in={isExpanded && subItems.open} timeout="auto" unmountOnExit>
                    <List component="div" disablePadding>
                        {subItems.items.map((item, index) => (
                            <ListItemButton key={index} sx={{ pl: 4 }} onClick={item.onClick}>
                                <Tooltip title={isExpanded ? '' : item.text} placement="right">
                                    <ListItemIcon>
                                        {item.icon}
                                    </ListItemIcon>
                                </Tooltip>
                                {isExpanded && <ListItemText primary={item.text} />}
                            </ListItemButton>
                        ))}
                    </List>
                </Collapse>
            )}
        </>
    );

    return (
        <Paper
            sx={{
                width: isExpanded ? 280 : 60,
                maxWidth: '100%',
                position: 'fixed',
                top: 100,
                left: 0,
                height: 'calc(100vh - 64px)',
                overflowY: 'auto',
                backgroundColor: theme.palette.background.default,
                borderRight: `1px solid ${theme.palette.divider}`,
                display: 'flex',
                flexDirection: 'column',
                transition: theme.transitions.create('width', {
                    easing: theme.transitions.easing.sharp,
                    duration: theme.transitions.duration.leavingScreen,
                }),
            }}
        >
            <Box sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                {isExpanded && (
                    <Typography variant="h6" color="primary" sx={{ fontWeight: 'bold' }}>
                        Menu Principal
                    </Typography>
                )}
                <IconButton onClick={toggleNavbar}>
                    {isExpanded ? <ChevronLeft /> : <ChevronRight />}
                </IconButton>
            </Box>
            <Divider />
            <List component="nav">
                {renderListItem(<Dashboard sx={{ color: theme.palette.primary.main }} />, "Tableau de bord", onDashboardClick)}
                {renderListItem(<People sx={{ color: theme.palette.secondary.main }} />, "Utilisateurs", handleUsersClick, {
                    open: openUsers,
                    items: [
                        { icon: <School />, text: "Tuteurs", onClick: () => onUserTypeClick('Tuteur') },
                        userData && userData.role === "admin" && { icon: <AdminPanelSettings />, text: "Administrateurs", onClick: () => onUserTypeClick('Administrateur') },
                        userData && userData.role === "admin" && { icon: <SupervisorAccount />, text: "Trackers", onClick: () => onUserTypeClick('Tracker') },
                        { icon: <PersonAdd />, text: "Ajouter un utilisateur", onClick: onAddUserClick },
                    ].filter(Boolean) // Filtre les éléments nuls ou indéfinis

                })}
                {renderListItem(<Class sx={{ color: theme.palette.success.main }} />, "Modules", handleModulesClick, {
                    open: openModules,
                    items: [
                        { icon: <Settings />, text: "Gérer les modules", onClick: onManageModulesClick },
                    ]
                })}
                {renderListItem(<Group sx={{ color: theme.palette.info.main }} />, "Groupes", handleGroupsClick, {
                    open: openGroups,
                    items: [
                        { icon: <Settings />, text: "Gérer les groupes", onClick: onManageGroupsClick },
                    ]
                })}
                {renderListItem(<TrackChanges sx={{ color: theme.palette.warning.main }} />, "Suivi des séances", handleSessionsClick, {
                    open: openSessions,
                    items: [
                        { icon: <Settings />, text: "Gérer les séances", onClick: onManageSessionsClick },
                    ]
                })}
                {renderListItem(<Assessment sx={{ color: theme.palette.success.dark }} />, "Rapports", onReportsClick)}
            </List>

            <Divider />

            <ListItemButton
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: 1,
                    position: 'fixed',
                    bottom: 0,
                    width: isExpanded ? 280 : 60,
                    py: 1,
                    px: 2,
                    backgroundColor: 'gray',
                    color: theme.palette.error.contrastText,
                    '&:hover': {
                        backgroundColor: 'black',
                    },
                    cursor: 'pointer',
                }}
            >
                <Tooltip title={isExpanded ? '' : "Déconnexion"} placement="right">
                    <ListItemIcon onClick={handleLogout}>
                        <Logout sx={{ color: theme.palette.error.contrastText }} />
                    </ListItemIcon>
                </Tooltip>
                {isExpanded && <ListItemText primary="Déconnexion" />}
            </ListItemButton>
        </Paper>
    );
};

export default NavbarLeft;