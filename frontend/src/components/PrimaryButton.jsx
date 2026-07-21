import React from 'react';
import LoadingSpinner from './LoadingSpinner';

const PrimaryButton = ({ text, onClick, type = 'button', isLoading = false, disabled = false, fullWidth = false }) => {
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled || isLoading}
      className={`flex justify-center items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-70 disabled:cursor-not-allowed transition-colors ${fullWidth ? 'w-full' : ''}`}
    >
      {isLoading ? <LoadingSpinner /> : text}
    </button>
  );
};

export default PrimaryButton;
