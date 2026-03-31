import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Counter } from 'k6/metrics';
import { uuidv4 } from './uuid.js';

// 커스텀 메트릭 (APM 연동용)
let apiResponseTime = new Trend('api_response_time', true);
let totalRequests = new Counter('total_requests');

// 부하 단계(stages)
export let options = {
    stages: [
        { duration: '30s', target: 10 },  // 점진적 Ramp-up
        { duration: '1m', target: 50 },   // 유지
        { duration: '30s', target: 0 },   // Ramp-down
    ],
};

export default function () {
    const rand = Math.random();

    let type;
    if (rand < 0.7) type = 'fast';
    else if (rand < 0.95) type = 'slow';
    else type = 'error';

    const url = `http://localhost:8080/async?type=${type}`
    const res = http.get(url, {
        headers: {
            'X-Trace-Id': uuidv4()
        }
    }); // 테스트 대상 API
    check(res, { 'status was 200': (r) => r.status === 200 });

    // 메트릭 기록
    apiResponseTime.add(res.timings.duration);
    totalRequests.add(1);

    sleep(1); // 사용자 간격
}