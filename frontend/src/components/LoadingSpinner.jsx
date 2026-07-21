import React from 'react';

const LoadingSpinner = () => {
  return (
    <div className="flex items-center justify-center space-x-2 animate-pulse">
      <div className="w-4 h-4 bg-indigo-500 rounded-full"></div>
      <div className="w-4 h-4 bg-indigo-500 rounded-full animation-delay-200"></div>
      <div className="w-4 h-4 bg-indigo-500 rounded-full animation-delay-400"></div>
    </div>
  );
};

export default LoadingSpinner;
