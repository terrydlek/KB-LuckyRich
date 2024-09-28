// DB에 transaction_date가 timestamp형이어서 만든 util입니다

export function convertTimestampToDate(timestamp) {
  if (timestamp instanceof Date) return timestamp;
  return new Date(timestamp);
}

export function isSameYearAndMonth(date, year, month) {
  return date.getFullYear() === year && date.getMonth() + 1 === month;
}

export function formatDate(date) {
  return date.toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
}