export interface Version {
  id: number;
  versionNumber: string;
  checksum: string;
  active: boolean;
  mandatory: boolean;
  minVersion: string;
  releaseNotes: string;
  createdBy: string;
  createdAt: string;
}
