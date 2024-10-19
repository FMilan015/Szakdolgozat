import React, { useEffect, useState } from 'react';
import axios from 'axios';

const Profile: React.FC = () => {
    const [userData, setUserData] = useState<any>(null);

    useEffect(() => {
        const fetchProfile = async () => {
            const token = localStorage.getItem('token');
            try {
                const response = await axios.get('http://localhost:8080/api/profile', {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                setUserData(response.data);
            } catch (error) {
                console.error('Error fetching profile data', error);
            }
        };

        fetchProfile();
    }, []);

    if (!userData) return <p>Loading...</p>;

    return (
        <div className="profile-page">
            <h1>Your Profile</h1>
            <p>Username: {userData.username}</p>
            <p>Email: {userData.email}</p>
        </div>
    );
};

export default Profile;
