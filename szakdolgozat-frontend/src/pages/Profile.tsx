import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './pages-style/Profile.css';

const Profile: React.FC = () => {
    const [userData, setUserData] = useState<any>(null);

    useEffect(() => {
        const fetchProfile = async () => {
            const token = localStorage.getItem('token');
            try {
                const response = await axios.get('http://localhost:8080/api/users/profile', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                console.log(token);
                setUserData(response.data);
            } catch (error) {
                console.error('Error fetching profile data on frontend', error);
            }
        };

        fetchProfile();
    }, []);

    if (!userData) return (
        <div className="profile-page">
            <p className="loading">
                Loading...
            </p>
        </div>
    );

    return (
        <div className="profile-page">
            <div className="profile-box">
                <h2>Your profile</h2>
                <table className="profile-info-table">
                    <thead>
                    <tr>
                        <th>Field</th>
                        <th>Value</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><strong>Username</strong></td>
                        <td>{userData.username}</td>
                    </tr>
                    <tr>
                        <td><strong>Email</strong></td>
                        <td>{userData.email}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Profile;
