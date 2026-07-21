import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';
import apiClient from '../api/apiClient';

const Dashboard = () => {
  const { user, updateUser } = useAuth();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    // Optionally fetch /me to get freshest user data on mount
    const fetchMe = async () => {
      try {
        setLoading(true);
        const response = await apiClient.get('/auth/me');
        if (response.data) {
          updateUser(response.data);
        }
      } catch (err) {
        console.error('Failed to fetch latest user data', err);
      } finally {
        setLoading(false);
      }
    };

    fetchMe();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col">
      <Navbar />
      
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-10">
        <div className="mb-8 border-b border-slate-200 pb-5">
          <h1 className="text-3xl font-bold text-slate-900">Dashboard</h1>
          <p className="mt-2 text-sm text-slate-500">Welcome back, {user?.firstName}!</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {/* User Profile Card */}
          <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-200 col-span-1">
            <h2 className="text-lg font-semibold text-slate-800 mb-4">Profile Information</h2>
            
            <div className="space-y-4">
              <div>
                <span className="block text-xs font-medium text-slate-500 uppercase tracking-wider">Full Name</span>
                <span className="block mt-1 text-sm font-medium text-slate-900">{user?.firstName} {user?.lastName}</span>
              </div>
              
              <div>
                <span className="block text-xs font-medium text-slate-500 uppercase tracking-wider">Email Address</span>
                <span className="block mt-1 text-sm font-medium text-slate-900">{user?.email}</span>
              </div>

              <div>
                <span className="block text-xs font-medium text-slate-500 uppercase tracking-wider">Department</span>
                <span className="block mt-1 text-sm font-medium text-slate-900">
                  {user?.department ? user.department.name : 'Unassigned'}
                </span>
              </div>

              <div>
                <span className="block text-xs font-medium text-slate-500 uppercase tracking-wider">System Roles</span>
                <div className="mt-2 flex flex-wrap gap-2">
                  {user?.systemRoles?.map((role, idx) => (
                    <span key={idx} className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800">
                      {role}
                    </span>
                  )) || <span className="text-sm text-slate-500">None</span>}
                </div>
              </div>
            </div>
          </div>

          {/* Workspaces Section */}
          <div className="col-span-1 md:col-span-2 space-y-6">
            <h2 className="text-lg font-semibold text-slate-800">Assigned Workspaces</h2>
            
            {loading ? (
              <div className="text-sm text-slate-500 animate-pulse">Loading workspaces...</div>
            ) : user?.workspaces && user.workspaces.length > 0 ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {user.workspaces.map((ws, idx) => (
                  <div key={ws.id || idx} className="bg-white p-5 rounded-xl shadow-sm border border-slate-200 hover:shadow-md transition-shadow">
                    <h3 className="text-md font-bold text-slate-900 truncate">{ws.name}</h3>
                    <div className="mt-3">
                      <span className="inline-flex items-center text-xs text-slate-500 uppercase tracking-wide">
                        Role: 
                        <span className="ml-2 font-medium text-indigo-700 bg-indigo-50 px-2 py-0.5 rounded">
                          {ws.workspaceRole || 'Member'}
                        </span>
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="bg-white p-8 rounded-xl border border-slate-200 border-dashed flex flex-col items-center justify-center text-center">
                <p className="text-sm font-medium text-slate-900">No Workspaces Assigned</p>
                <p className="mt-1 text-sm text-slate-500">You currently do not have access to any workspaces.</p>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;
