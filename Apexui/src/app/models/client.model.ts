export interface Client {
  id: string;
  name: string;
  location: string;
  clientVersion: string;
  status: 'ONLINE' | 'OFFLINE';
  lastSeen: string;
  createdAt: string;
}
